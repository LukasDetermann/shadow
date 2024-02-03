package io.determann.shadow.internal.lang_model;

import io.determann.shadow.api.lang_model.LangModelConstants;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Null;
import io.determann.shadow.api.shadow.Primitive;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.Wildcard;

import static io.determann.shadow.api.lang_model.LangModelAdapter.getShadow;
import static io.determann.shadow.api.lang_model.LangModelAdapter.getTypes;
import static javax.lang.model.type.TypeKind.*;

public class LangModelConstantsImpl implements LangModelConstants
{
   private final LangModelContext context;

   LangModelConstantsImpl(LangModelContext context)
   {
      this.context = context;
   }

   @Override
   public Wildcard getUnboundWildcard()
   {
      return getShadow(context, getTypes(context).getWildcardType(null, null));
   }

   @Override
   public Null getNull()
   {
      return getShadow(context, getTypes(context).getNullType());
   }

   @Override
   public Void getVoid()
   {
      return getShadow(context, getTypes(context).getNoType(VOID));
   }

   @Override
   public Primitive getPrimitiveBoolean()
   {
      return getShadow(context, getTypes(context).getPrimitiveType(BOOLEAN));
   }

   @Override
   public Primitive getPrimitiveByte()
   {
      return getShadow(context, getTypes(context).getPrimitiveType(BYTE));
   }

   @Override
   public Primitive getPrimitiveShort()
   {
      return getShadow(context, getTypes(context).getPrimitiveType(SHORT));
   }

   @Override
   public Primitive getPrimitiveInt()
   {
      return getShadow(context, getTypes(context).getPrimitiveType(INT));
   }

   @Override
   public Primitive getPrimitiveLong()
   {
      return getShadow(context, getTypes(context).getPrimitiveType(LONG));
   }

   @Override
   public Primitive getPrimitiveChar()
   {
      return getShadow(context, getTypes(context).getPrimitiveType(CHAR));
   }

   @Override
   public Primitive getPrimitiveFloat()
   {
      return getShadow(context, getTypes(context).getPrimitiveType(FLOAT));
   }

   @Override
   public Primitive getPrimitiveDouble()
   {
      return getShadow(context, getTypes(context).getPrimitiveType(DOUBLE));
   }
}
