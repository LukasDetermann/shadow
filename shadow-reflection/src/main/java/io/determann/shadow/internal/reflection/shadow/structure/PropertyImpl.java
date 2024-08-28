package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.structure.R_Field;
import io.determann.shadow.api.reflection.shadow.structure.R_Method;
import io.determann.shadow.api.reflection.shadow.structure.R_Property;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.structure.C_Property;

import java.util.Optional;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

/**
 * implementation note: the casts to LangModel types are safe
 */
public class PropertyImpl implements R_Property
{
   private final C_Property delegate;

   public PropertyImpl(C_Property delegate)
   {
      this.delegate = delegate;
   }

   @Override
   public String getName()
   {
      return requestOrThrow(delegate, PROPERTY_GET_NAME);
   }

   @Override
   public R_Type getType()
   {
      return (R_Type) requestOrThrow(delegate, PROPERTY_GET_TYPE);
   }

   @Override
   public Optional<R_Field> getField()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_FIELD).map(R_Field.class::cast);
   }

   @Override
   public R_Field getFieldOrThrow()
   {
      return getField().orElseThrow();
   }

   @Override
   public R_Method getGetter()
   {
      return (R_Method) requestOrThrow(delegate, PROPERTY_GET_GETTER);
   }

   @Override
   public Optional<R_Method> getSetter()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_SETTER).map(R_Method.class::cast);
   }

   @Override
   public R_Method getSetterOrThrow()
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
