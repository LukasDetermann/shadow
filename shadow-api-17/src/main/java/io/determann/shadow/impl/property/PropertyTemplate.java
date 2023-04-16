package io.determann.shadow.impl.property;

import io.determann.shadow.api.shadow.Field;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.TypeMirror;
import java.util.Objects;

class PropertyTemplate
{
   private final String name;
   private final Shadow<TypeMirror> type;
   private Method setter;
   private final Method getter;
   private Field field;

   PropertyTemplate(String name, Shadow<TypeMirror> type, Method getter)
   {
      this.name = name;
      this.type = type;
      this.getter = getter;
   }

   String getName()
   {
      return name;
   }

   Shadow<TypeMirror> getType()
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
   public boolean equals(Object o)
   {
      return this == o ||
             o instanceof PropertyTemplate that &&
             Objects.equals(setter, that.setter) &&
             Objects.equals(getter, that.getter) &&
             Objects.equals(field, that.field);
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
