package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.structure.FieldLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.MethodLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.PropertyLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;
import io.determann.shadow.api.shadow.structure.Property;

import java.util.Optional;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrEmpty;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;

/**
 * implementation note: the casts to LangModel types are safe
 */
public class PropertyImpl implements PropertyLangModel
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
   public ShadowLangModel getType()
   {
      return (ShadowLangModel) requestOrThrow(delegate, PROPERTY_GET_TYPE);
   }

   @Override
   public Optional<FieldLangModel> getField()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_FIELD).map(FieldLangModel.class::cast);
   }

   @Override
   public FieldLangModel getFieldOrThrow()
   {
      return getField().orElseThrow();
   }

   @Override
   public MethodLangModel getGetter()
   {
      return (MethodLangModel) requestOrThrow(delegate, PROPERTY_GET_GETTER);
   }

   @Override
   public Optional<MethodLangModel> getSetter()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_SETTER).map(MethodLangModel.class::cast);
   }

   @Override
   public MethodLangModel getSetterOrThrow()
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
