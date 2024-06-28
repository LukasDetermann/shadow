package io.determann.shadow.implementation.support.internal.property;

import io.determann.shadow.api.shadow.property.Property;
import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PropertyImpl implements Property
{
   private final String name;
   private final Shadow shadow;
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
                                    .toList();
   }

   private PropertyImpl(String name,
                        Shadow shadow,
                        Field field,
                        Method getter,
                        Method setter)
   {
      this.name = name;
      this.shadow = shadow;
      this.field = field;
      this.getter = getter;
      this.setter = setter;
   }

   @Override
   public String getName()
   {
      return name;
   }

   @Override
   public Shadow getType()
   {
      return shadow;
   }

   @Override
   public Optional<Field> getField()
   {
      return Optional.ofNullable(field);
   }

   @Override
   public Field getFieldOrThrow()
   {
      return getField().orElseThrow();
   }

   @Override
   public Method getGetter()
   {
      return getter;
   }

   @Override
   public Method getSetterOrThrow()
   {
      return getSetter().orElseThrow();
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
   public int hashCode()
   {
      return Objects.hash(getType(), getName());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Property otherProperty))
      {
         return false;
      }
      return Objects.equals(getType(), otherProperty.getType()) &&
             Objects.equals(getName(), otherProperty.getName());
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
