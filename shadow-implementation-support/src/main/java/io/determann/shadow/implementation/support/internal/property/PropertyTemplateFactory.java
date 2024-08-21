package io.determann.shadow.implementation.support.internal.property;

import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.structure.Parameter;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.shadow.TypeKind.CLASS;
import static io.determann.shadow.api.shadow.TypeKind.VOID;
import static io.determann.shadow.implementation.support.internal.property.PropertyTemplateFactory.AccessorType.GETTER;
import static io.determann.shadow.implementation.support.internal.property.PropertyTemplateFactory.AccessorType.SETTER;
import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class PropertyTemplateFactory
{
   private static class Accessor
   {
      private final Method method;
      private final AccessorType type;
      private final String prefix;
      private final String name;
      private final int position;

      private Accessor(Method method, AccessorType type, String prefix, String name, int position)
      {
         this.method = method;
         this.type = type;
         this.prefix = prefix;
         this.name = name;
         this.position = position;
      }

      public Method getMethod() {return method;}

      public AccessorType getType() {return type;}

      public String getPrefix() {return prefix;}

      public String getName() {return name;}

      public int getPosition() {return position;}
   }

   enum AccessorType
   {
      GETTER,
      SETTER,
   }

   private static final String GET_PREFIX = "get";
   private static final String SET_PREFIX = "set";
   private static final String IS_PREFIX = "is";

   private PropertyTemplateFactory() {}

   public static List<PropertyTemplate> templatesFor(Declared declared)
   {
      Map<String, Field> nameField = requestOrThrow(declared, DECLARED_GET_FIELDS).stream().collect(Collectors.toMap(field -> requestOrThrow(field,
                                                                                                                                             NAMEABLE_GET_NAME), Function.identity()));

      //we should keep the ordering
      AtomicInteger position = new AtomicInteger();
      Map<String, Map<AccessorType, List<Accessor>>> nameTypeAccessors =
            getMethods(declared).stream()
                                .filter(method -> !requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, Modifier.STATIC))
                                .map(method1 -> toAccessor(method1, position.getAndIncrement()))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .collect(groupingBy(Accessor::getName,
                                                    groupingBy(Accessor::getType)));

      return nameTypeAccessors.entrySet().stream()
                              .filter(entry -> entry.getValue().containsKey(GETTER))
                              .map(entry ->
                                   {
                                      Accessor getter = findGetter(entry.getValue());
                                      String name = entry.getKey();
                                      Shadow shadow = requestOrThrow(getter.getMethod(), EXECUTABLE_GET_RETURN_TYPE);

                                      PropertyTemplate template = new PropertyTemplate(name, shadow, getter.getMethod());

                                      findSetter(entry.getValue(), shadow).ifPresent(template::setSetter);
                                      findField(nameField, shadow, name).ifPresent(template::setField);

                                      return new AbstractMap.SimpleEntry<>(getter.getPosition(), template);
                                   })
                              .sorted((Map.Entry.comparingByKey()))
                              .map(Map.Entry::getValue)
                              .toList();
   }

   private static List<? extends Method> getMethods(Declared declared)
   {
      if (!requestOrThrow(declared, SHADOW_GET_KIND).equals(CLASS))
      {
         return requestOrThrow(declared, DECLARED_GET_METHODS);
      }
      List<Class> superClasses = Stream.iterate(((Class) declared),
                                                Objects::nonNull,
                                                aClass -> requestOrThrow(aClass, CLASS_GET_SUPER_CLASS)).collect(toList());

      Collections.reverse(superClasses);

      List<? extends Method> methods = superClasses.stream()
                                         .flatMap(aClass -> requestOrThrow(aClass, DECLARED_GET_METHODS).stream())
                                         .toList();

      return methods.stream()
                    .filter(method -> methods.stream().noneMatch(method1 -> requestOrThrow(method, METHOD_OVERWRITTEN_BY, method1)))
                    .toList();
   }

   private static Optional<Field> findField(Map<String, Field> nameField, Shadow shadow, String name)
   {
      Field field = nameField.get(name);
      if (field == null || !requestOrThrow(requestOrThrow(field, VARIABLE_GET_TYPE), SHADOW_REPRESENTS_SAME_TYPE, shadow))
      {
         return Optional.empty();
      }
      return Optional.of(field);
   }

   private static Optional<Method> findSetter(Map<AccessorType, List<Accessor>> typeAccessors, Shadow shadow)
   {
      List<Accessor> setters = typeAccessors.get(SETTER);
      if (setters == null ||
          setters.size() != 1 ||
          !requestOrThrow(requestOrThrow(requestOrThrow(setters.get(0).getMethod(), EXECUTABLE_GET_PARAMETERS).get(0), VARIABLE_GET_TYPE), SHADOW_REPRESENTS_SAME_TYPE, shadow))
      {
         return Optional.empty();
      }
      return Optional.of(setters.get(0).getMethod());
   }

   private static Accessor findGetter(Map<AccessorType, List<Accessor>> typeAccessors)
   {
      List<Accessor> getters = typeAccessors.get(GETTER);

      if (getters == null || getters.size() > 2)
      {
         throw new IllegalStateException();
      }
      if (getters.size() == 1)
      {
         return getters.get(0);
      }
      if (getters.size() == 2)
      {
         for (Accessor accessor : getters)
         {
            //prefer is getter over get getter
            if (accessor.getPrefix().equals(IS_PREFIX))
            {
               return accessor;
            }
         }
      }
      throw new IllegalStateException();
   }

   private static Optional<Accessor> toAccessor(Method method, int position)
   {
      String name = requestOrThrow(method, NAMEABLE_GET_NAME);
      List<? extends Parameter> parameters = requestOrThrow(method, EXECUTABLE_GET_PARAMETERS);
      Shadow returnType = requestOrThrow(method, EXECUTABLE_GET_RETURN_TYPE);

      //getter
      if (!requestOrThrow(returnType, SHADOW_GET_KIND).equals(VOID))
      {
         boolean hasGetPrefix = name.startsWith(GET_PREFIX) && name.length() > 3;
         boolean hasIsPrefix = requestOrThrow(returnType, SHADOW_GET_KIND).equals(TypeKind.BOOLEAN) &&
                               name.startsWith(IS_PREFIX) &&
                               name.length() > 2;

         if (parameters.isEmpty())
         {
            if (hasGetPrefix)
            {
               return Optional.of(new Accessor(method, GETTER, GET_PREFIX, toPropertyName(method, GET_PREFIX), position));
            }
            if (hasIsPrefix)
            {
               return Optional.of(new Accessor(method, GETTER, IS_PREFIX, toPropertyName(method, IS_PREFIX), position));
            }
         }
         return Optional.empty();
      }
      //setter
      boolean couldBeSetter = requestOrThrow(returnType, SHADOW_GET_KIND).equals(VOID) &&
                              name.startsWith(SET_PREFIX) &&
                              name.length() > 3;

      if (couldBeSetter && parameters.size() == 1)
      {
         return Optional.of(new Accessor(method, SETTER, SET_PREFIX, toPropertyName(method, SET_PREFIX), position));
      }
      return Optional.empty();
   }

   private static String toPropertyName(Method method, String prefix)
   {
      String name = requestOrThrow(method, NAMEABLE_GET_NAME).substring(prefix.length());

      //java beans 8.8
      if (name.length() > 1 && isUpperCase(name.charAt(0)) && isUpperCase(name.charAt(1)))
      {
         return name;
      }
      return toLowerCase(name.charAt(0)) + name.substring(1);
   }
}