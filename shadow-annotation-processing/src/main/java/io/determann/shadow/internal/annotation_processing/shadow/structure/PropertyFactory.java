package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.Modifier;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.determann.shadow.internal.annotation_processing.shadow.structure.PropertyFactory.AccessorType.GETTER;
import static io.determann.shadow.internal.annotation_processing.shadow.structure.PropertyFactory.AccessorType.SETTER;
import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;
import static java.util.stream.Collectors.*;

public class PropertyFactory
{
   private record Accessor(Ap.Method method,
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

   public static List<Ap.Property> of(Ap.Declared declared)
   {
      Map<String, Ap.Field> nameField = declared.getFields().stream()
                                                .filter(field -> !field.hasModifier(Modifier.STATIC))
                                                .collect(toMap(Ap.Nameable::getName, Function.identity()));

      //we should keep the ordering
      AtomicInteger position = new AtomicInteger();
      Map<String, Map<AccessorType, List<Accessor>>> nameTypeAccessors =
            getMethods(declared).stream()
                                .filter(method -> !method.hasModifier(Modifier.STATIC))
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
                                      Ap.VariableType type = ((Ap.VariableType) getter.method().getReturnType());

                                      Ap.Method setter = findSetter(entry.getValue(), type).orElse(null);
                                      Ap.Field field = findField(nameField, type, name).orElse(null);

                                      PropertyImpl template = new PropertyImpl(name, type, field, getter.method(), setter);

                                      return new AbstractMap.SimpleEntry<>(getter.position(), template);
                                   })
                              .sorted((Map.Entry.comparingByKey()))
                              .map(Map.Entry::getValue)
                              .map(Ap.Property.class::cast)
                              .toList();
   }

   private static List<? extends Ap.Method> getMethods(Ap.Declared declared)
   {
      if (!(declared instanceof Ap.Class aClass))
      {
         return declared.getMethods();
      }
      List<Ap.Class> superClasses = Stream.iterate((aClass),
                                                   Objects::nonNull,
                                                   Ap.Class::getSuperClass).collect(toList());

      Collections.reverse(superClasses);

      List<? extends Ap.Method> methods = superClasses.stream()
                                                      .map(Ap.Declared::getMethods)
                                                      .flatMap(Collection::stream)
                                                      .toList();

      return methods.stream()
                    .filter(method -> methods.stream().noneMatch(method::overwrittenBy))
                    .toList();
   }

   private static Optional<Ap.Field> findField(Map<String, Ap.Field> nameField, Ap.Type type, String name)
   {
      Ap.Field field = nameField.get(name);
      if (field == null || !field.getType().representsSameType(type))
      {
         return Optional.empty();
      }
      return Optional.of(field);
   }

   private static Optional<Ap.Method> findSetter(Map<AccessorType, List<Accessor>> typeAccessors, Ap.Type type)
   {
      List<Accessor> setters = typeAccessors.get(SETTER);
      if (setters == null ||
          setters.size() != 1 ||
          !setters.get(0).method().getParameters().get(0).getType().representsSameType(type))
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

   private static Optional<Accessor> toAccessor(Ap.Method method, int position)
   {
      String name = method.getName();
      List<? extends Ap.Parameter> parameters = method.getParameters();
      Ap.Type returnType = method.getReturnType();

      //getter
      if (!(returnType instanceof Ap.Void))
      {
         boolean hasGetPrefix = name.startsWith(GET_PREFIX) && name.length() > 3;
         boolean hasIsPrefix = (returnType instanceof Ap.boolean_ ||
                                returnType instanceof Ap.Declared declared &&
                                "java.lang.Boolean".equals(declared.getQualifiedName())) &&
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

   private static String toPropertyName(Ap.Method method, String prefix)
   {
      String name = method.getName().substring(prefix.length());

      //java beans 8.8
      if (name.length() > 1 && isUpperCase(name.charAt(0)) && isUpperCase(name.charAt(1)))
      {
         return name;
      }
      return toLowerCase(name.charAt(0)) + name.substring(1);
   }
}