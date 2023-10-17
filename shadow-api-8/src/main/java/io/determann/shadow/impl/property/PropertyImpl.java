package io.determann.shadow.impl.property;

import io.determann.shadow.api.property.Property;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Field;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.api.shadow.Shadow;

import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class PropertyImpl implements Property
{
   private final String name;
   private final Shadow type;
   private final Field field;
   private final Method getter;
   private final Method setter;

   public static List<Property> of(Declared declared)
   {
      return PropertyTemplateFactory.templatesFor(declared).stream()
                                    .map(template -> new PropertyImpl(template.getName(),
                                                                      template.getType(),
                                                                      template.getField(),
                                                                      template.getGetter(),
                                                                      template.getSetter()))
                                    .map(Property.class::cast)
                                    .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   private PropertyImpl(String name,
                        Shadow type,
                        Field field,
                        Method getter,
                        Method setter)
   {
      this.name = name;
      this.type = type;
      this.field = field;
      this.getter = getter;
      this.setter = setter;
   }

   @Override
   public String getSimpleName()
   {
      return name;
   }

   @Override
   public Shadow getType()
   {
      return type;
   }

   @Override
   public Optional<Field> getField()
   {
      return Optional.ofNullable(field);
   }

   @Override
   public Field getFieldOrThrow()
   {
      return getField().orElseThrow(NoSuchElementException::new);
   }

   @Override
   public Method getGetter()
   {
      return getter;
   }

   @Override
   public Method getSetterOrThrow()
   {
      return getSetter().orElseThrow(NoSuchElementException::new);
   }

   @Override
   public Optional<Method> getSetter()
   {
      return Optional.ofNullable(setter);
   }

   @Override
   public boolean isMutable()
   {
      return getSetter().isPresent();
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (!(o instanceof PropertyImpl))
      {
         return false;
      }
      PropertyImpl property = (PropertyImpl) o;
      return Objects.equals(field, property.field) &&
             Objects.equals(getter, property.getter) &&
             Objects.equals(setter, property.setter);
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(field, getter, setter);
   }

   @Override
   public String toString()
   {
      return "PropertyImpl{" +
             "field=" + field +
             ", getter=" + getter +
             ", setter=" + setter +
             '}';
   }
}
