package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Field;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Method;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Property;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.shadow.structure.C_Property;

import java.util.Optional;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;

/**
 * implementation note: the casts to LangModel types are safe
 */
public class PropertyImpl implements LM_Property
{
   private final C_Property delegate;
   private final LM_Context context;

   public PropertyImpl(LM_Context api, C_Property delegate)
   {
      this.delegate = delegate;
      this.context = api;
   }

   @Override
   public String getName()
   {
      return requestOrThrow(delegate, PROPERTY_GET_NAME);
   }

   @Override
   public LM_Type getType()
   {
      return (LM_Type) requestOrThrow(delegate, PROPERTY_GET_TYPE);
   }

   @Override
   public Optional<LM_Field> getField()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_FIELD).map(LM_Field.class::cast);
   }

   @Override
   public LM_Field getFieldOrThrow()
   {
      return getField().orElseThrow();
   }

   @Override
   public LM_Method getGetter()
   {
      return (LM_Method) requestOrThrow(delegate, PROPERTY_GET_GETTER);
   }

   @Override
   public Optional<LM_Method> getSetter()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_SETTER).map(LM_Method.class::cast);
   }

   @Override
   public LM_Method getSetterOrThrow()
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
