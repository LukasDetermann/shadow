package io.determann.shadow.internal.lang_model;

import io.determann.shadow.api.lang_model.LM_Constants;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.adapter.LM_Adapters;
import io.determann.shadow.api.lang_model.shadow.type.LM_Wildcard;
import io.determann.shadow.api.lang_model.shadow.type.primitive.LM_Primitive;
import io.determann.shadow.api.shadow.type.C_Null;
import io.determann.shadow.api.shadow.type.C_Void;

import static io.determann.shadow.api.lang_model.adapter.LM_Adapters.adapt;
import static javax.lang.model.type.TypeKind.*;

public class LangModelConstantsImpl implements LM_Constants
{
   private final LM_Context context;

   LangModelConstantsImpl(LM_Context context)
   {
      this.context = context;
   }

   @Override
   public LM_Wildcard getUnboundWildcard()
   {
      return LM_Adapters.adapt(context, adapt(context).toTypes().getWildcardType(null, null));
   }

   @Override
   public C_Null getNull()
   {
      return LM_Adapters.adapt(context, adapt(context).toTypes().getNullType());
   }

   @Override
   public C_Void getVoid()
   {
      return LM_Adapters.adapt(context, adapt(context).toTypes().getNoType(VOID));
   }

   @Override
   public LM_Primitive getPrimitiveBoolean()
   {
      return LM_Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(BOOLEAN));
   }

   @Override
   public LM_Primitive getPrimitiveByte()
   {
      return LM_Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(BYTE));
   }

   @Override
   public LM_Primitive getPrimitiveShort()
   {
      return LM_Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(SHORT));
   }

   @Override
   public LM_Primitive getPrimitiveInt()
   {
      return LM_Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(INT));
   }

   @Override
   public LM_Primitive getPrimitiveLong()
   {
      return LM_Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(LONG));
   }

   @Override
   public LM_Primitive getPrimitiveChar()
   {
      return LM_Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(CHAR));
   }

   @Override
   public LM_Primitive getPrimitiveFloat()
   {
      return LM_Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(FLOAT));
   }

   @Override
   public LM_Primitive getPrimitiveDouble()
   {
      return LM_Adapters.adapt(context, adapt(context).toTypes().getPrimitiveType(DOUBLE));
   }
}
