package io.determann.shadow.internal.lang_model;

import io.determann.shadow.api.lang_model.Constants;
import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;

import static io.determann.shadow.api.lang_model.adapter.Adapters.adapt;
import static javax.lang.model.type.TypeKind.*;

public class LangModelConstantsImpl implements Constants
{
   private final LM.Context context;

   LangModelConstantsImpl(LM.Context context)
   {
      this.context = context;
   }

   @Override
   public LM.Wildcard getUnboundWildcard()
   {
      return Adapters.adapt(context, adapt(context).toTypes().getWildcardType(null, null));
   }

   @Override
   public LM.Null getNull()
   {
      return Adapters.adapt(context, adapt(context).toTypes().getNullType());
   }

   @Override
   public LM.Void getVoid()
   {
      return Adapters.adapt(context, adapt(context).toTypes().getNoType(VOID));
   }

   @Override
   public LM.boolean_ getPrimitiveBoolean()
   {
      return (LM.boolean_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(BOOLEAN));
   }

   @Override
   public LM.byte_ getPrimitiveByte()
   {
      return (LM.byte_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(BYTE));
   }

   @Override
   public LM.short_ getPrimitiveShort()
   {
      return (LM.short_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(SHORT));
   }

   @Override
   public LM.int_ getPrimitiveInt()
   {
      return (LM.int_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(INT));
   }

   @Override
   public LM.long_ getPrimitiveLong()
   {
      return (LM.long_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(LONG));
   }

   @Override
   public LM.char_ getPrimitiveChar()
   {
      return (LM.char_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(CHAR));
   }

   @Override
   public LM.float_ getPrimitiveFloat()
   {
      return (LM.float_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(FLOAT));
   }

   @Override
   public LM.double_ getPrimitiveDouble()
   {
      return (LM.double_) Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(DOUBLE));
   }
}
