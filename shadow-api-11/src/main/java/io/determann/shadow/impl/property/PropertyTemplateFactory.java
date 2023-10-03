package io.determann.shadow.impl.property;

import io.determann.shadow.api.ElementBacked;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.determann.shadow.api.ShadowApi.convert;
import static io.determann.shadow.api.TypeKind.CLASS;
import static io.determann.shadow.api.TypeKind.VOID;
import static io.determann.shadow.impl.property.PropertyTemplateFactory.AccessorType.GETTER;
import static io.determann.shadow.impl.property.PropertyTemplateFactory.AccessorType.SETTER;
import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

class PropertyTemplateFactory
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

   static List<PropertyTemplate> templatesFor(Declared declared)
   {
      Map<String, Field> nameField = declared.getFields().stream().collect(Collectors.toMap(ElementBacked::getSimpleName, Function.identity()));

      //we should keep the ordering
      AtomicInteger position = new AtomicInteger();
      Map<String, Map<AccessorType, List<Accessor>>> nameTypeAccessors =
            getMethods(declared).stream()
                                .filter(method -> !method.isStatic())
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
                                      Shadow type = getter.getMethod().getReturnType();

                                      PropertyTemplate template = new PropertyTemplate(name, type, getter.getMethod());

                                      findSetter(entry.getValue(), type).ifPresent(template::setSetter);
                                      findField(nameField, type, name).ifPresent(template::setField);

                                      return new AbstractMap.SimpleEntry<>(getter.getPosition(), template);
                                   })
                              .sorted((Map.Entry.comparingByKey()))
                              .map(Map.Entry::getValue)
                              .collect(toList());
   }

   private static List<Method> getMethods(Declared declared)
   {
      if (!declared.isTypeKind(CLASS))
      {
         return declared.getMethods();
      }
      List<Class> superClasses = Stream.iterate(convert(declared).toClassOrThrow(), Objects::nonNull, Class::getSuperClass).collect(toList());

      Collections.reverse(superClasses);

      List<Method> methods = superClasses.stream()
                                         .flatMap(aClass -> aClass.getMethods().stream())
                                         .collect(toList());

      return methods.stream()
                    .filter(method -> methods.stream().noneMatch(method::overwrittenBy))
                    .collect(toList());
   }

   private static Optional<Field> findField(Map<String, Field> nameField, Shadow type, String name)
   {
      Field field = nameField.get(name);
      if (field == null || !field.getType().representsSameType(type))
      {
         return Optional.empty();
      }
      return Optional.of(field);
   }

   private static Optional<Method> findSetter(Map<AccessorType, List<Accessor>> typeAccessors, Shadow type)
   {
      List<Accessor> setters = typeAccessors.get(SETTER);
      if (setters == null || setters.size() != 1 || !setters.get(0).getMethod().getParameters().get(0).getType().representsSameType(type))
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
      String name = method.getSimpleName();
      List<Parameter> parameters = method.getParameters();

      //getter
      if (!method.getReturnType().isTypeKind(VOID))
      {
         boolean hasGetPrefix = name.startsWith(GET_PREFIX) && name.length() > 3;
         boolean hasIsPrefix = method.getReturnType().isTypeKind(TypeKind.BOOLEAN) && name.startsWith(IS_PREFIX) && name.length() > 2;

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
      boolean couldBeSetter = method.getReturnType().isTypeKind(VOID) && name.startsWith(SET_PREFIX) && name.length() > 3;
      if (couldBeSetter && parameters.size() == 1)
      {
         return Optional.of(new Accessor(method, SETTER, SET_PREFIX, toPropertyName(method, SET_PREFIX), position));
      }
      return Optional.empty();
   }

   private static String toPropertyName(Method method, String prefix)
   {
      String name = method.getSimpleName().substring(prefix.length());

      //java beans 8.8
      if (name.length() > 1 && isUpperCase(name.charAt(0)) && isUpperCase(name.charAt(1)))
      {
         return name;
      }
      return toLowerCase(name.charAt(0)) + name.substring(1);
   }
}