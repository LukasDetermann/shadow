package org.determann.shadow.impl.property;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.property.MutableProperty;
import org.determann.shadow.api.shadow.Declared;
import org.determann.shadow.api.shadow.Field;
import org.determann.shadow.api.shadow.Method;
import org.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

public class MutablePropertyImpl implements MutableProperty
{
   private final ShadowApi api;
   private final String name;
   private final Shadow<TypeMirror> type;
   private final Field field;
   private final Method getter;
   private final Method setter;

   public static List<MutableProperty> of(Declared declared)
   {
      return PropertyTemplateFactory.templatesFor(declared).stream().filter(template -> template.getSetter() != null)
                                    .map(template -> new MutablePropertyImpl(declared.getApi(),
                                                                  template.getName(),
                                                                  template.getType(),
                                                                  template.getField(),
                                                                  template.getGetter(),
                                                                  template.getSetter()))
                                    .map(MutableProperty.class::cast)
                                    .toList();
   }

   private MutablePropertyImpl(ShadowApi api,
                               String name,
                               Shadow<TypeMirror> type,
                               Field field,
                               Method getter,
                               Method setter)
   {
      this.name = name;
      this.type = type;
      this.api = api;
      this.field = field;
      this.getter = getter;
      this.setter = setter;
   }

   @Override
   public ShadowApi getApi()
   {
      return api;
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
   public Method getSetter()
   {
      return setter;
   }
}
