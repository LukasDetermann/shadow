package io.determann.shadow.impl.property;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.property.Property;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Field;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PropertyImpl implements Property
{
   private final String name;
   private final Shadow<TypeMirror> type;
   private final ShadowApi shadowApi;
   private final Field field;
   private final Method getter;
   private final Method setter;

   public static List<Property> of(Declared declared)
   {
      return PropertyTemplateFactory.templatesFor(declared).stream()
                                    .map(template -> new PropertyImpl(declared.getApi(),
                                                                      template.getName(),
                                                                      template.getType(),
                                                                      template.getField(),
                                                                      template.getGetter(),
                                                                      template.getSetter()))
                                    .map(Property.class::cast)
                                    .toList();
   }

   private PropertyImpl(ShadowApi shadowApi,
                        String name,
                        Shadow<TypeMirror> type,
                        Field field,
                        Method getter,
                        Method setter)
   {
      this.shadowApi = shadowApi;
      this.name = name;
      this.type = type;
      this.field = field;
      this.getter = getter;
      this.setter = setter;
   }

   @Override
   public ShadowApi getApi()
   {
      return shadowApi;
   }

   @Override
   public String getSimpleName()
   {
      return name;
   }

   @Override
   public Shadow<TypeMirror> getType()
   {
      return type;
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
   public Method getSetterOrThrow()
   {
      return getSetter().orElseThrow();
   }

   @Override
   public Optional<Method> getSetter()
   {
      return Optional.ofNullable(setter);
   }

   @Override
   public boolean isMutable()
   {
      return getSetter().isPresent();
   }

   @Override
   public boolean equals(Object o)
   {
      return this == o ||
             o instanceof PropertyImpl property &&
             Objects.equals(field, property.field) &&
             Objects.equals(getter, property.getter) &&
             Objects.equals(setter, property.setter);
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(field, getter, setter);
   }

   @Override
   public String toString()
   {
      return "PropertyImpl{" +
             "field=" + field +
             ", getter=" + getter +
             ", setter=" + setter +
             '}';
   }
}