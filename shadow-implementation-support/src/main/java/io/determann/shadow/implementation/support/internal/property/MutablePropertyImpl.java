package io.determann.shadow.implementation.support.internal.property;

import io.determann.shadow.api.shadow.property.MutableProperty;
import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MutablePropertyImpl implements MutableProperty
{
   private final String name;
   private final Shadow shadow;
   private final Field field;
   private final Method getter;
   private final Method setter;

   public static List<MutableProperty> of(Declared declared)
   {
      return PropertyTemplateFactory.templatesFor(declared).stream().filter(template -> template.getSetter() != null)
                                    .map(template -> new MutablePropertyImpl(template.getName(),
                                                                             template.getType(),
                                                                             template.getField(),
                                                                             template.getGetter(),
                                                                             template.getSetter()))
                                    .map(MutableProperty.class::cast)
                                    .toList();
   }

   private MutablePropertyImpl(String name,
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
   public Method getSetter()
   {
      return setter;
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
      if (!(other instanceof MutableProperty otherMutableProperty))
      {
         return false;
      }
      return Objects.equals(getType(), otherMutableProperty.getType()) &&
             Objects.equals(getName(), otherMutableProperty.getName());
   }
}
