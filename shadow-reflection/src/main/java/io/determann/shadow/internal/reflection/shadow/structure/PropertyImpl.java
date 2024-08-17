package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.structure.FieldReflection;
import io.determann.shadow.api.reflection.shadow.structure.MethodReflection;
import io.determann.shadow.api.reflection.shadow.structure.PropertyReflection;
import io.determann.shadow.api.reflection.shadow.type.ShadowReflection;
import io.determann.shadow.api.shadow.structure.Property;

import java.util.Optional;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrEmpty;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

/**
 * implementation note: the casts to LangModel types are safe
 */
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
   public ShadowReflection getType()
   {
      return (ShadowReflection) requestOrThrow(delegate, PROPERTY_GET_TYPE);
   }

   @Override
   public Optional<FieldReflection> getField()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_FIELD).map(FieldReflection.class::cast);
   }

   @Override
   public FieldReflection getFieldOrThrow()
   {
      return getField().orElseThrow();
   }

   @Override
   public MethodReflection getGetter()
   {
      return (MethodReflection) requestOrThrow(delegate, PROPERTY_GET_GETTER);
   }

   @Override
   public Optional<MethodReflection> getSetter()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_SETTER).map(MethodReflection.class::cast);
   }

   @Override
   public MethodReflection getSetterOrThrow()
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
