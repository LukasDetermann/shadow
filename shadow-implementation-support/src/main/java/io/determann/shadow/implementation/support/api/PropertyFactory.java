package io.determann.shadow.implementation.support.api;

import io.determann.shadow.api.shadow.property.ImmutableProperty;
import io.determann.shadow.api.shadow.property.MutableProperty;
import io.determann.shadow.api.shadow.property.Property;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.implementation.support.internal.property.ImmutablePropertyImpl;
import io.determann.shadow.implementation.support.internal.property.MutablePropertyImpl;
import io.determann.shadow.implementation.support.internal.property.PropertyImpl;

import java.util.List;

public class PropertyFactory
{
   public static List<Property> propertyOf(Declared declared)
   {
      return PropertyImpl.of(declared);
   }

   public static List<ImmutableProperty> immutablePropertyOf(Declared declared)
   {
      return ImmutablePropertyImpl.of(declared);
   }

   public static List<MutableProperty> mutablePropertyOf(Declared declared)
   {
      return MutablePropertyImpl.of(declared);
   }
}
