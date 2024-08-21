package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.type.LM_Class;
import io.determann.shadow.api.lang_model.shadow.type.LM_Primitive;
import io.determann.shadow.api.lang_model.shadow.type.LM_Shadow;
import io.determann.shadow.api.shadow.C_TypeKind;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.PrimitiveSupport;

import javax.lang.model.type.PrimitiveType;

public class PrimitiveImpl extends ShadowImpl<PrimitiveType> implements LM_Primitive
{
   public PrimitiveImpl(LM_Context context, PrimitiveType primitiveTypeMirror)
   {
      super(context, primitiveTypeMirror);
   }

   @Override
   public boolean isSubtypeOf(C_Shadow shadow)
   {
      return LM_Adapter.getTypes(getApi()).isSubtype(LM_Adapter.particularType((LM_Shadow) shadow), getMirror());
   }

   @Override
   public boolean isAssignableFrom(C_Shadow shadow)
   {
      return LM_Adapter.getTypes(getApi()).isAssignable(getMirror(), LM_Adapter.particularType((LM_Shadow) shadow));
   }

   @Override
   public LM_Class asBoxed()
   {
      return LM_Adapter
                     .generalize(getApi(), LM_Adapter.getTypes(getApi()).boxedClass(getMirror()).asType());
   }

   @Override
   public C_TypeKind getKind()
   {
      return switch (getMirror().getKind())
      {
         case BOOLEAN -> C_TypeKind.BOOLEAN;
         case BYTE -> C_TypeKind.BYTE;
         case SHORT -> C_TypeKind.SHORT;
         case INT -> C_TypeKind.INT;
         case LONG -> C_TypeKind.LONG;
         case CHAR -> C_TypeKind.CHAR;
         case FLOAT -> C_TypeKind.FLOAT;
         case DOUBLE -> C_TypeKind.DOUBLE;
         default -> throw new IllegalStateException();
      };
   }

   @Override
   public String getName()
   {
      return switch (getKind())
      {
         case BOOLEAN -> "boolean";
         case BYTE -> "byte";
         case SHORT -> "short";
         case INT -> "int";
         case LONG -> "long";
         case CHAR -> "char";
         case FLOAT -> "float";
         case DOUBLE -> "double";
         default -> throw new IllegalStateException();
      };
   }

   @Override
   public int hashCode()
   {
      return PrimitiveSupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return PrimitiveSupport.equals(this, other);
   }

   @Override
   public String toString()
   {
      return PrimitiveSupport.toString(this);
   }
}
