package io.determann.shadow.impl.property;

import io.determann.shadow.api.shadow.Field;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.api.shadow.Shadow;

import java.util.Objects;

class PropertyTemplate
{
   private final String name;
   private final Shadow type;
   private Method setter;
   private final Method getter;
   private Field field;

   PropertyTemplate(String name, Shadow type, Method getter)
   {
      this.name = name;
      this.type = type;
      this.getter = getter;
   }

   String getName()
   {
      return name;
   }

   Shadow getType()
   {
      return type;
   }

   Method getSetter()
   {
      return setter;
   }

   void setSetter(Method setter)
   {
      this.setter = setter;
   }

   Method getGetter()
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
             ", type=" + type +
             ", setter=" + setter +
             ", getter=" + getter +
             ", field=" + field +
             '}';
   }
}
