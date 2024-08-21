package io.determann.shadow.implementation.support.internal.property;

import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.structure.C_Property;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.implementation.support.api.shadow.structure.PropertySupport;

import java.util.List;
import java.util.Optional;

import static io.determann.shadow.implementation.support.internal.SupportProvider.IMPLEMENTATION_NAME;

public class PropertyImpl implements C_Property
{
   private final String name;
   private final C_Shadow shadow;
   private final C_Field field;
   private final C_Method getter;
   private final C_Method setter;

   public static List<C_Property> of(C_Declared declared)
   {
      return PropertyTemplateFactory.templatesFor(declared).stream()
                                    .map(template -> new PropertyImpl(template.getName(),
                                                                      template.getType(),
                                                                      template.getField(),
                                                                      template.getGetter(),
                                                                      template.getSetter()))
                                    .map(C_Property.class::cast)
                                    .toList();
   }

   private PropertyImpl(String name,
                        C_Shadow shadow,
                        C_Field field,
                        C_Method getter,
                        C_Method setter)
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

   public C_Shadow getType()
   {
      return shadow;
   }

   public Optional<C_Field> getField()
   {
      return Optional.ofNullable(field);
   }

   public C_Method getGetter()
   {
      return getter;
   }

   public Optional<C_Method> getSetter()
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
