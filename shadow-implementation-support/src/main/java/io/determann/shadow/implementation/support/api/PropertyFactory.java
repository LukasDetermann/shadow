package io.determann.shadow.implementation.support.api;

import io.determann.shadow.api.shadow.structure.Property;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.implementation.support.internal.property.PropertyImpl;

import java.util.List;

public class PropertyFactory
{
   public static List<Property> propertyOf(Declared declared)
   {
      return PropertyImpl.of(declared);
   }
}
