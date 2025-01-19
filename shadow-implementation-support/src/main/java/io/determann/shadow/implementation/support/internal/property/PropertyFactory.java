package io.determann.shadow.implementation.support.internal.property;

import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.structure.C_Property;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.api.shadow.type.C_Void;
import io.determann.shadow.api.shadow.type.primitive.C_boolean;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.implementation.support.internal.property.PropertyFactory.AccessorType.GETTER;
import static io.determann.shadow.implementation.support.internal.property.PropertyFactory.AccessorType.SETTER;
import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;
import static java.util.stream.Collectors.*;

public class PropertyFactory
{
   private static record Accessor(C_Method method,
                                  AccessorType type,
                                  String prefix,
                                  String name,
                                  int position) {}

   enum AccessorType
   {
      GETTER,
      SETTER,
   }

   private static final String GET_PREFIX = "get";
   private static final String SET_PREFIX = "set";
   private static final String IS_PREFIX = "is";

   private PropertyFactory() {}

   public static List<C_Property> of(C_Declared declared)
   {
      Map<String, C_Field> nameField =
            requestOrThrow(declared, DECLARED_GET_FIELDS).stream()
                                                         .filter(field -> !requestOrThrow(field, MODIFIABLE_HAS_MODIFIER, C_Modifier.STATIC))
                                                         .collect(toMap(field -> requestOrThrow(field, NAMEABLE_GET_NAME),
                                                                        Function.identity()));

      //we should keep the ordering
      AtomicInteger position = new AtomicInteger();
      Map<String, Map<AccessorType, List<Accessor>>> nameTypeAccessors =
            getMethods(declared).stream()
                                .filter(method -> !requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, C_Modifier.STATIC))
                                .map(method1 -> toAccessor(method1, position.getAndIncrement()))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .collect(groupingBy(Accessor::name,
                                                    groupingBy(Accessor::type)));

      return nameTypeAccessors.entrySet().stream()
                              .filter(entry -> entry.getValue().containsKey(GETTER))
                              .map(entry ->
                                   {
                                      Accessor getter = findGetter(entry.getValue());
                                      String name = entry.getKey();
                                      C_Type type = requestOrThrow(getter.method(), METHOD_GET_RETURN_TYPE);

                                      C_Method setter = findSetter(entry.getValue(), type).orElse(null);
                                      C_Field field = findField(nameField, type, name).orElse(null);

                                      PropertyImpl template = new PropertyImpl(name, type, field, getter.method(), setter);

                                      return new AbstractMap.SimpleEntry<>(getter.position(), template);
                                   })
                              .sorted((Map.Entry.comparingByKey()))
                              .map(Map.Entry::getValue)
                              .map(C_Property.class::cast)
                              .toList();
   }

   private static List<? extends C_Method> getMethods(C_Declared declared)
   {
      if (!(declared instanceof C_Class aClass))
      {
         return requestOrThrow(declared, DECLARED_GET_METHODS);
      }
      List<C_Class> superClasses = Stream.iterate((aClass),
                                                  Objects::nonNull,
                                                  aClass1 -> requestOrThrow(aClass1, CLASS_GET_SUPER_CLASS)).collect(toList());

      Collections.reverse(superClasses);

      List<? extends C_Method> methods = superClasses.stream()
                                                     .flatMap(aClass1 -> requestOrThrow(aClass1, DECLARED_GET_METHODS).stream())
                                                     .toList();

      return methods.stream()
                    .filter(method -> methods.stream().noneMatch(method1 -> requestOrThrow(method, METHOD_OVERWRITTEN_BY, method1)))
                    .toList();
   }

   private static Optional<C_Field> findField(Map<String, C_Field> nameField, C_Type type, String name)
   {
      C_Field field = nameField.get(name);
      if (field == null || !requestOrThrow(requestOrThrow(field, VARIABLE_GET_TYPE), TYPE_REPRESENTS_SAME_TYPE, type))
      {
         return Optional.empty();
      }
      return Optional.of(field);
   }

   private static Optional<C_Method> findSetter(Map<AccessorType, List<Accessor>> typeAccessors, C_Type type)
   {
      List<Accessor> setters = typeAccessors.get(SETTER);
      if (setters == null ||
          setters.size() != 1 ||
          !requestOrThrow(requestOrThrow(requestOrThrow(setters.get(0).method(), EXECUTABLE_GET_PARAMETERS).get(0), VARIABLE_GET_TYPE),
                          TYPE_REPRESENTS_SAME_TYPE, type))
      {
         return Optional.empty();
      }
      return Optional.of(setters.get(0).method());
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
            if (accessor.prefix().equals(IS_PREFIX))
            {
               return accessor;
            }
         }
      }
      throw new IllegalStateException();
   }

   private static Optional<Accessor> toAccessor(C_Method method, int position)
   {
      String name = requestOrThrow(method, NAMEABLE_GET_NAME);
      List<? extends C_Parameter> parameters = requestOrThrow(method, EXECUTABLE_GET_PARAMETERS);
      C_Type returnType = requestOrThrow(method, METHOD_GET_RETURN_TYPE);

      //getter
      if (!(returnType instanceof C_Void))
      {
         boolean hasGetPrefix = name.startsWith(GET_PREFIX) && name.length() > 3;
         boolean hasIsPrefix = (returnType instanceof C_boolean ||
                                returnType instanceof C_Declared declared &&
                                "java.lang.Boolean".equals(requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME))) &&
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
      boolean couldBeSetter = name.startsWith(SET_PREFIX) && name.length() > 3;

      if (couldBeSetter && parameters.size() == 1)
      {
         return Optional.of(new Accessor(method, SETTER, SET_PREFIX, toPropertyName(method, SET_PREFIX), position));
      }
      return Optional.empty();
   }

   private static String toPropertyName(C_Method method, String prefix)
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