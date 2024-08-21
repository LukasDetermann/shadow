package io.determann.shadow.internal.lang_model;

import io.determann.shadow.api.lang_model.LM_Constants;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.type.LM_Primitive;
import io.determann.shadow.api.lang_model.shadow.type.LM_Wildcard;
import io.determann.shadow.api.shadow.type.C_Null;
import io.determann.shadow.api.shadow.type.C_Void;

import static io.determann.shadow.api.lang_model.LM_Adapter.generalize;
import static io.determann.shadow.api.lang_model.LM_Adapter.getTypes;
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
      return generalize(context, getTypes(context).getWildcardType(null, null));
   }

   @Override
   public C_Null getNull()
   {
      return generalize(context, getTypes(context).getNullType());
   }

   @Override
   public C_Void getVoid()
   {
      return generalize(context, getTypes(context).getNoType(VOID));
   }

   @Override
   public LM_Primitive getPrimitiveBoolean()
   {
      return generalize(context, getTypes(context).getPrimitiveType(BOOLEAN));
   }

   @Override
   public LM_Primitive getPrimitiveByte()
   {
      return generalize(context, getTypes(context).getPrimitiveType(BYTE));
   }

   @Override
   public LM_Primitive getPrimitiveShort()
   {
      return generalize(context, getTypes(context).getPrimitiveType(SHORT));
   }

   @Override
   public LM_Primitive getPrimitiveInt()
   {
      return generalize(context, getTypes(context).getPrimitiveType(INT));
   }

   @Override
   public LM_Primitive getPrimitiveLong()
   {
      return generalize(context, getTypes(context).getPrimitiveType(LONG));
   }

   @Override
   public LM_Primitive getPrimitiveChar()
   {
      return generalize(context, getTypes(context).getPrimitiveType(CHAR));
   }

   @Override
   public LM_Primitive getPrimitiveFloat()
   {
      return generalize(context, getTypes(context).getPrimitiveType(FLOAT));
   }

   @Override
   public LM_Primitive getPrimitiveDouble()
   {
      return generalize(context, getTypes(context).getPrimitiveType(DOUBLE));
   }
}
