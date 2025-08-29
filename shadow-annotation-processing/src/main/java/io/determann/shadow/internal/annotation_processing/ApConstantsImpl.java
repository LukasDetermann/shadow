package io.determann.shadow.internal.annotation_processing;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.Constants;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static javax.lang.model.type.TypeKind.*;

public class ApConstantsImpl
      implements Constants
{
   private final Ap.Context context;

   ApConstantsImpl(Ap.Context context)
   {
      this.context = context;
   }

   @Override
   public Ap.Wildcard getUnboundWildcard()
   {
      return Adapters.adapt(context, adapt(context).toTypes().getWildcardType(null, null));
   }

   @Override
   public Ap.Null getNull()
   {
      return Adapters.adapt(context, adapt(context).toTypes().getNullType());
   }

   @Override
   public Ap.Void getVoid()
   {
      return Adapters.adapt(context, adapt(context).toTypes().getNoType(VOID));
   }

   @Override
   public Ap.boolean_ getPrimitiveBoolean()
   {
      return (Ap.boolean_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(BOOLEAN));
   }

   @Override
   public Ap.byte_ getPrimitiveByte()
   {
      return (Ap.byte_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(BYTE));
   }

   @Override
   public Ap.short_ getPrimitiveShort()
   {
      return (Ap.short_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(SHORT));
   }

   @Override
   public Ap.int_ getPrimitiveInt()
   {
      return (Ap.int_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(INT));
   }

   @Override
   public Ap.long_ getPrimitiveLong()
   {
      return (Ap.long_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(LONG));
   }

   @Override
   public Ap.char_ getPrimitiveChar()
   {
      return (Ap.char_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(CHAR));
   }

   @Override
   public Ap.float_ getPrimitiveFloat()
   {
      return (Ap.float_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(FLOAT));
   }

   @Override
   public Ap.double_ getPrimitiveDouble()
   {
      return (Ap.double_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(DOUBLE));
   }
}
