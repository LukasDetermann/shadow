package org.determann.shadow.impl.shadow.wraper;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Field;
import org.determann.shadow.api.shadow.Method;
import org.determann.shadow.api.wrapper.Property;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PropertyImpl implements Property
{
   private final ShadowApi shadowApi;
   private final Field field;
   private final Method getter;
   private final Method setter;

   public static List<Property> of(ShadowApi shadowApi, Class aClass)
   {
      List<Field> fields = aClass.getFields();
      List<Method> methods = aClass.getMethods();

      return fields.stream()
                   .filter(fieldWrapper -> !fieldWrapper.isStatic())
                   .map(field ->
                        {
                           Method getter = null;
                           Method setter = null;
                           for (Method method : methods)
                           {
                              String fieldName = shadowApi.to_UpperCamelCase(field.getSimpleName());
                              if (method.getSimpleName().equals("set" + fieldName))
                              {
                                 setter = method;
                              }
                              else if (method.getSimpleName().equals("get" + fieldName) || method.getSimpleName().equals("is" + fieldName))
                              {
                                 getter = method;
                              }
                              if (getter != null)
                              {
                                 if (setter != null)
                                 {
                                    return Optional.of(new PropertyImpl(shadowApi, field, getter, setter));
                                 }
                                 if (field.isFinal())
                                 {
                                    return Optional.of(new PropertyImpl(shadowApi, field, getter, null));
                                 }
                              }
                           }
                           return Optional.<Property>empty();
                        })
                   .filter(Optional::isPresent)
                   .map(Optional::get)
                   .map(Property.class::cast)
                   .toList();
   }

   private PropertyImpl(ShadowApi shadowApi, Field field, Method getter, Method setter)
   {
      this.shadowApi = shadowApi;
      this.field = field;
      this.getter = getter;
      this.setter = setter;
   }

   @Override
   public Field getField()
   {
      return field;
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
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (other == null || !getClass().equals(other.getClass()))
      {
         return false;
      }
      PropertyImpl otherProperty = (PropertyImpl) other;
      return Objects.equals(getField(), otherProperty.getField()) &&
             Objects.equals(getGetter(), otherProperty.getGetter()) &&
             Objects.equals(getSetter(), otherProperty.getSetter());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getField(), getGetter(), getSetter());
   }

   @Override
   public String toString()
   {
      return "Property{" +
             "field=" + field +
             ", getter=" + getter +
             ", setter=" + setter +
             '}';
   }

   @Override
   public ShadowApi getApi()
   {
      return shadowApi;
   }
}
