package io.determann.shadow.implementation.support.internal.property;

import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.Objects;

public class PropertyTemplate
{
   private final String name;
   private final C_Type type;
   private C_Method setter;
   private final C_Method getter;
   private C_Field field;

   PropertyTemplate(String name, C_Type type, C_Method getter)
   {
      this.name = name;
      this.type = type;
      this.getter = getter;
   }

   public String getName()
   {
      return name;
   }

   public C_Type getType()
   {
      return type;
   }

   public C_Method getSetter()
   {
      return setter;
   }

   void setSetter(C_Method setter)
   {
      this.setter = setter;
   }

   public C_Method getGetter()
   {
      return getter;
   }

   C_Field getField()
   {
      return field;
   }

   void setField(C_Field field)
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
