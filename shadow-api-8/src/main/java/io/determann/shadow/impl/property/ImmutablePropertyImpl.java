package io.determann.shadow.impl.property;

import io.determann.shadow.api.property.ImmutableProperty;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Field;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.api.shadow.Shadow;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class ImmutablePropertyImpl implements ImmutableProperty
{
   private final String name;
   private final Shadow type;
   private final Field field;
   private final Method getter;

   public static List<ImmutableProperty> of(Declared declared)
   {
      return PropertyTemplateFactory.templatesFor(declared).stream()
                                    .filter(template -> template.getSetter() == null)
                                    .map(template -> new ImmutablePropertyImpl(template.getName(),
                                                                               template.getType(),
                                                                               template.getField(),
                                                                               template.getGetter()))
                                    .map(ImmutableProperty.class::cast)
                                    .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   private ImmutablePropertyImpl(String name, Shadow type, Field field, Method getter)
   {
      this.name = name;
      this.type = type;
      this.field = field;
      this.getter = getter;
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
      return getField().orElseThrow(NoSuchElementException::new);
   }

   @Override
   public Method getGetter()
   {
      return getter;
   }
}
