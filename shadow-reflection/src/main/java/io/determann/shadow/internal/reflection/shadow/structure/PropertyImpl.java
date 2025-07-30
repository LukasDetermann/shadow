package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.reflection.R;

import java.util.Optional;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;

/**
 * implementation note: the casts to LangModel types are safe
 */
public class PropertyImpl implements R.Property
{
   private final C.Property delegate;

   public PropertyImpl(C.Property delegate)
   {
      this.delegate = delegate;
   }

   @Override
   public String getName()
   {
      return requestOrThrow(delegate, NAMEABLE_GET_NAME);
   }

   @Override
   public R.VariableType getType()
   {
      return (R.VariableType) requestOrThrow(delegate, PROPERTY_GET_TYPE);
   }

   @Override
   public Optional<R.Field> getField()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_FIELD).map(R.Field.class::cast);
   }

   @Override
   public R.Field getFieldOrThrow()
   {
      return getField().orElseThrow();
   }

   @Override
   public R.Method getGetter()
   {
      return (R.Method) requestOrThrow(delegate, PROPERTY_GET_GETTER);
   }

   @Override
   public Optional<R.Method> getSetter()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_SETTER).map(R.Method.class::cast);
   }

   @Override
   public R.Method getSetterOrThrow()
   {
      return getSetter().orElseThrow();
   }

   @Override
   public boolean isMutable()
   {
      return requestOrThrow(delegate, PROPERTY_IS_MUTABLE);
   }

   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}
