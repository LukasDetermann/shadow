package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.query.Implementation;

import java.util.Optional;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static io.determann.shadow.api.query.Provider.requestOrThrow;

/**
 * implementation note: the casts to LangModel types are safe
 */
public class PropertyImpl implements Ap.Property
{
   private final C.Property delegate;
   private final Ap.Context context;

   public PropertyImpl(Ap.Context api, C.Property delegate)
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
   public Ap.VariableType getType()
   {
      return (Ap.VariableType) requestOrThrow(delegate, PROPERTY_GET_TYPE);
   }

   @Override
   public Optional<Ap.Field> getField()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_FIELD).map(Ap.Field.class::cast);
   }

   @Override
   public Ap.Field getFieldOrThrow()
   {
      return getField().orElseThrow();
   }

   @Override
   public Ap.Method getGetter()
   {
      return (Ap.Method) requestOrThrow(delegate, PROPERTY_GET_GETTER);
   }

   @Override
   public Optional<Ap.Method> getSetter()
   {
      return requestOrEmpty(delegate, PROPERTY_GET_SETTER).map(Ap.Method.class::cast);
   }

   @Override
   public Ap.Method getSetterOrThrow()
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
