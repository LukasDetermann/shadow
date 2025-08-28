package io.determann.shadow.internal.annotation_processing;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.api.annotation_processing.Constants;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static javax.lang.model.type.TypeKind.*;

public class ApConstantsImpl
      implements Constants
{
   private final AP.Context context;

   ApConstantsImpl(AP.Context context)
   {
      this.context = context;
   }

   @Override
   public AP.Wildcard getUnboundWildcard()
   {
      return Adapters.adapt(context, adapt(context).toTypes().getWildcardType(null, null));
   }

   @Override
   public AP.Null getNull()
   {
      return Adapters.adapt(context, adapt(context).toTypes().getNullType());
   }

   @Override
   public AP.Void getVoid()
   {
      return Adapters.adapt(context, adapt(context).toTypes().getNoType(VOID));
   }

   @Override
   public AP.boolean_ getPrimitiveBoolean()
   {
      return (AP.boolean_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(BOOLEAN));
   }

   @Override
   public AP.byte_ getPrimitiveByte()
   {
      return (AP.byte_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(BYTE));
   }

   @Override
   public AP.short_ getPrimitiveShort()
   {
      return (AP.short_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(SHORT));
   }

   @Override
   public AP.int_ getPrimitiveInt()
   {
      return (AP.int_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(INT));
   }

   @Override
   public AP.long_ getPrimitiveLong()
   {
      return (AP.long_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(LONG));
   }

   @Override
   public AP.char_ getPrimitiveChar()
   {
      return (AP.char_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(CHAR));
   }

   @Override
   public AP.float_ getPrimitiveFloat()
   {
      return (AP.float_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(FLOAT));
   }

   @Override
   public AP.double_ getPrimitiveDouble()
   {
      return (AP.double_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(DOUBLE));
   }
}
