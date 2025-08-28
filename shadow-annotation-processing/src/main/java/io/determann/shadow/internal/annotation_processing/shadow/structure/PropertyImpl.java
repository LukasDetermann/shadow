package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.api.query.Implementation;

import java.util.Optional;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static io.determann.shadow.api.query.Provider.requestOrThrow;

/**
 * implementation note: the casts to LangModel types are safe
 */
public class PropertyImpl implements AP.Property
{
   private final C.Property delegate;
   private final AP.Context context;

   public PropertyImpl(AP.Context api, C.Property delegate)
   {
      this.delegate = delegate;
      this.context = api;
   }

   @Override
   public String getName()
   {
      return requestOrThrow(delegate, NAMEABLE_GET_NAME);
   }

   @Override
   public AP.VariableType getType()
   {
      return (AP.VariableType) requestOrThrow(delegate, PROPERTY_GET_TYPE);
   }

   @Override
   public Optional<AP.Field> getField()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_FIELD).map(AP.Field.class::cast);
   }

   @Override
   public AP.Field getFieldOrThrow()
   {
      return getField().orElseThrow();
   }

   @Override
   public AP.Method getGetter()
   {
      return (AP.Method) requestOrThrow(delegate, PROPERTY_GET_GETTER);
   }

   @Override
   public Optional<AP.Method> getSetter()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_SETTER).map(AP.Method.class::cast);
   }

   @Override
   public AP.Method getSetterOrThrow()
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
      return context.getImplementation();
   }
}
