package io.determann.shadow.internal.lang_model;

import io.determann.shadow.api.lang_model.LangModelConstants;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.type.PrimitiveLangModel;
import io.determann.shadow.api.lang_model.shadow.type.WildcardLangModel;
import io.determann.shadow.api.shadow.type.Null;
import io.determann.shadow.api.shadow.type.Void;

import static io.determann.shadow.api.lang_model.LangModelAdapter.generalize;
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
   public WildcardLangModel getUnboundWildcard()
   {
      return generalize(context, getTypes(context).getWildcardType(null, null));
   }

   @Override
   public Null getNull()
   {
      return generalize(context, getTypes(context).getNullType());
   }

   @Override
   public Void getVoid()
   {
      return generalize(context, getTypes(context).getNoType(VOID));
   }

   @Override
   public PrimitiveLangModel getPrimitiveBoolean()
   {
      return generalize(context, getTypes(context).getPrimitiveType(BOOLEAN));
   }

   @Override
   public PrimitiveLangModel getPrimitiveByte()
   {
      return generalize(context, getTypes(context).getPrimitiveType(BYTE));
   }

   @Override
   public PrimitiveLangModel getPrimitiveShort()
   {
      return generalize(context, getTypes(context).getPrimitiveType(SHORT));
   }

   @Override
   public PrimitiveLangModel getPrimitiveInt()
   {
      return generalize(context, getTypes(context).getPrimitiveType(INT));
   }

   @Override
   public PrimitiveLangModel getPrimitiveLong()
   {
      return generalize(context, getTypes(context).getPrimitiveType(LONG));
   }

   @Override
   public PrimitiveLangModel getPrimitiveChar()
   {
      return generalize(context, getTypes(context).getPrimitiveType(CHAR));
   }

   @Override
   public PrimitiveLangModel getPrimitiveFloat()
   {
      return generalize(context, getTypes(context).getPrimitiveType(FLOAT));
   }

   @Override
   public PrimitiveLangModel getPrimitiveDouble()
   {
      return generalize(context, getTypes(context).getPrimitiveType(DOUBLE));
   }
}
