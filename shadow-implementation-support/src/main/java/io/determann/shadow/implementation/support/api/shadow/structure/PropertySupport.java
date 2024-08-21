package io.determann.shadow.implementation.support.api.shadow.structure;

import io.determann.shadow.api.shadow.structure.Property;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.implementation.support.internal.property.PropertyImpl;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import java.util.List;

import static io.determann.shadow.api.Operations.PROPERTY_GET_NAME;
import static io.determann.shadow.api.Operations.PROPERTY_GET_TYPE;

public class PropertySupport
{
   public static List<Property> propertiesOf(Declared declared)
   {
      return PropertyImpl.of(declared);
   }

   public static boolean equals(Property property, Object other)
   {
      return SupportSupport.equals(property, Property.class, other, PROPERTY_GET_NAME, PROPERTY_GET_TYPE);
   }

   public static int hashCode(Property property)
   {
      return SupportSupport.hashCode(property, PROPERTY_GET_NAME, PROPERTY_GET_TYPE);
   }

   public static String toString(Property property)
   {
      return SupportSupport.toString(property, Property.class, PROPERTY_GET_NAME, PROPERTY_GET_TYPE);
   }
}
