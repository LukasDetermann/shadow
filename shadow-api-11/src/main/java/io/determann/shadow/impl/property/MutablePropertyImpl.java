package io.determann.shadow.impl.property;

import io.determann.shadow.api.property.MutableProperty;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Field;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.api.shadow.Shadow;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toUnmodifiableList;

public class MutablePropertyImpl implements MutableProperty
{
   private final String name;
   private final Shadow type;
   private final Field field;
   private final Method getter;
   private final Method setter;

   public static List<MutableProperty> of(Declared declared)
   {
      return PropertyTemplateFactory.templatesFor(declared).stream().filter(template -> template.getSetter() != null)
                                    .map(template -> new MutablePropertyImpl(template.getName(),
                                                                             template.getType(),
                                                                             template.getField(),
                                                                             template.getGetter(),
                                                                             template.getSetter()))
                                    .map(MutableProperty.class::cast)
                                    .collect(toUnmodifiableList());
   }

   private MutablePropertyImpl(String name,
                               Shadow type,
                               Field field,
                               Method getter,
                               Method setter)
   {
      this.name = name;
      this.type = type;
      this.field = field;
      this.getter = getter;
      this.setter = setter;
   }

   @Override
   public String getName()
   {
      return name;
   }

   @Override
   public Shadow getType()
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
