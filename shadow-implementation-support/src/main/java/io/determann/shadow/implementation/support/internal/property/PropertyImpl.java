package io.determann.shadow.implementation.support.internal.property;

import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.structure.Property;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.api.shadow.structure.PropertySupport;

import java.util.List;
import java.util.Optional;

import static io.determann.shadow.implementation.support.internal.SupportProvider.IMPLEMENTATION_NAME;

public class PropertyImpl implements Property
{
   private final String name;
   private final Shadow shadow;
   private final Field field;
   private final Method getter;
   private final Method setter;

   public static List<Property> of(Declared declared)
   {
      return PropertyTemplateFactory.templatesFor(declared).stream()
                                    .map(template -> new PropertyImpl(template.getName(),
                                                                      template.getType(),
                                                                      template.getField(),
                                                                      template.getGetter(),
                                                                      template.getSetter()))
                                    .map(Property.class::cast)
                                    .toList();
   }

   private PropertyImpl(String name,
                        Shadow shadow,
                        Field field,
                        Method getter,
                        Method setter)
   {
      this.name = name;
      this.shadow = shadow;
      this.field = field;
      this.getter = getter;
      this.setter = setter;
   }

   public String getName()
   {
      return name;
   }

   public Shadow getType()
   {
      return shadow;
   }

   public Optional<Field> getField()
   {
      return Optional.ofNullable(field);
   }

   public Method getGetter()
   {
      return getter;
   }

   public Optional<Method> getSetter()
   {
      return Optional.ofNullable(setter);
   }

   public boolean isMutable()
   {
      return getSetter().isPresent();
   }

   @Override
   public int hashCode()
   {
      return PropertySupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return PropertySupport.equals(this, other);
   }

   @Override
   public String toString()
   {
      return PropertySupport.toString(this);
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
