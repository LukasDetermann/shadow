package io.determann.shadow.implementation.support.api.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.implementation.support.internal.property.PropertyFactory;
import io.determann.shadow.implementation.support.internal.shadow.SupportSupport;

import java.util.List;

import static io.determann.shadow.api.query.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.query.Operations.PROPERTY_GET_TYPE;

public class PropertySupport
{
   public static List<C.Property> propertiesOf(C.Declared declared)
   {
      return PropertyFactory.of(declared);
   }

   public static boolean equals(C.Property property, Object other)
   {
      return SupportSupport.equals(property, C.Property.class, other, NAMEABLE_GET_NAME, PROPERTY_GET_TYPE);
   }

   public static int hashCode(C.Property property)
   {
      return SupportSupport.hashCode(property, NAMEABLE_GET_NAME, PROPERTY_GET_TYPE);
   }

   public static String toString(C.Property property)
   {
      return SupportSupport.toString(property, C.Property.class, NAMEABLE_GET_NAME, PROPERTY_GET_TYPE);
   }
}
