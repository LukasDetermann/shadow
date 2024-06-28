package io.determann.shadow.implementation.support.internal.property;

import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.Objects;

public class PropertyTemplate
{
   private final String name;
   private final Shadow shadow;
   private Method setter;
   private final Method getter;
   private Field field;

   PropertyTemplate(String name, Shadow shadow, Method getter)
   {
      this.name = name;
      this.shadow = shadow;
      this.getter = getter;
   }

   public String getName()
   {
      return name;
   }

   public Shadow getType()
   {
      return shadow;
   }

   public Method getSetter()
   {
      return setter;
   }

   void setSetter(Method setter)
   {
      this.setter = setter;
   }

   public Method getGetter()
   {
      return getter;
   }

   Field getField()
   {
      return field;
   }

   void setField(Field field)
   {
      this.field = field;
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof PropertyTemplate otherPropertyTemplate))
      {
         return false;
      }
      return Objects.equals(setter, otherPropertyTemplate.setter) &&
             Objects.equals(getter, otherPropertyTemplate.getter) &&
             Objects.equals(field, otherPropertyTemplate.field);
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(setter, getter, field);
   }

   @Override
   public String toString()
   {
      return "PropertyTemplate{" +
             "name='" + name + '\'' +
             ", type=" + shadow +
             ", setter=" + setter +
             ", getter=" + getter +
             ", field=" + field +
             '}';
   }
}
