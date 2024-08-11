package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.structure.PropertyReflection;
import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.structure.Property;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.Optional;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrEmpty;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class PropertyImpl implements PropertyReflection
{
   private final Property delegate;

   public PropertyImpl(Property delegate)
   {
      this.delegate = delegate;
   }

   @Override
   public String getName()
   {
      return requestOrThrow(delegate, PROPERTY_GET_NAME);
   }

   @Override
   public Shadow getType()
   {
      return requestOrThrow(delegate, PROPERTY_GET_TYPE);
   }

   @Override
   public Optional<Field> getField()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_FIELD);
   }

   @Override
   public Field getFieldOrThrow()
   {
      return getField().orElseThrow();
   }

   @Override
   public Method getGetter()
   {
      return requestOrThrow(delegate, PROPERTY_GET_GETTER);
   }

   @Override
   public Optional<Method> getSetter()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_SETTER);
   }

   @Override
   public Method getSetterOrThrow()
   {
      return getSetter().orElseThrow();
   }

   @Override
   public boolean isMutable()
   {
      return requestOrThrow(delegate, PROPERTY_IS_MUTABLE);
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
