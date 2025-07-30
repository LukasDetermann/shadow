package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.query.Implementation;

import java.util.Optional;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static io.determann.shadow.api.query.Provider.requestOrThrow;

/**
 * implementation note: the casts to LangModel types are safe
 */
public class PropertyImpl implements LM.Property
{
   private final C.Property delegate;
   private final LM.Context context;

   public PropertyImpl(LM.Context api, C.Property delegate)
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
   public LM.VariableType getType()
   {
      return (LM.VariableType) requestOrThrow(delegate, PROPERTY_GET_TYPE);
   }

   @Override
   public Optional<LM.Field> getField()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_FIELD).map(LM.Field.class::cast);
   }

   @Override
   public LM.Field getFieldOrThrow()
   {
      return getField().orElseThrow();
   }

   @Override
   public LM.Method getGetter()
   {
      return (LM.Method) requestOrThrow(delegate, PROPERTY_GET_GETTER);
   }

   @Override
   public Optional<LM.Method> getSetter()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_SETTER).map(LM.Method.class::cast);
   }

   @Override
   public LM.Method getSetterOrThrow()
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
