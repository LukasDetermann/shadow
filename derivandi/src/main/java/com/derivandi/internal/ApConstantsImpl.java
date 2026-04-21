package com.derivandi.internal;

import com.derivandi.api.D;
import com.derivandi.api.adapter.Adapters;
import com.derivandi.api.processor.Constants;
import com.derivandi.api.processor.SimpleContext;

import static com.derivandi.api.adapter.Adapters.adapt;
import static javax.lang.model.type.TypeKind.*;

public class ApConstantsImpl
      implements Constants
{
   private final SimpleContext context;

   public ApConstantsImpl(SimpleContext context)
   {
      this.context = context;
   }

   @Override
   public D.Wildcard getUnboundWildcard()
   {
      return Adapters.adapt(context, adapt(context).toTypes().getWildcardType(null, null));
   }

   @Override
   public D.Null getNull()
   {
      return Adapters.adapt(context, adapt(context).toTypes().getNullType());
   }

   @Override
   public D.Void getVoid()
   {
      return Adapters.adapt(context, adapt(context).toTypes().getNoType(VOID));
   }

   @Override
   public D.boolean_ getPrimitiveBoolean()
   {
      return (D.boolean_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(BOOLEAN));
   }

   @Override
   public D.byte_ getPrimitiveByte()
   {
      return (D.byte_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(BYTE));
   }

   @Override
   public D.short_ getPrimitiveShort()
   {
      return (D.short_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(SHORT));
   }

   @Override
   public D.int_ getPrimitiveInt()
   {
      return (D.int_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(INT));
   }

   @Override
   public D.long_ getPrimitiveLong()
   {
      return (D.long_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(LONG));
   }

   @Override
   public D.char_ getPrimitiveChar()
   {
      return (D.char_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(CHAR));
   }

   @Override
   public D.float_ getPrimitiveFloat()
   {
      return (D.float_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(FLOAT));
   }

   @Override
   public D.double_ getPrimitiveDouble()
   {
      return (D.double_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(DOUBLE));
   }
}
