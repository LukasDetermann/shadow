package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.type.ClassLangModel;
import io.determann.shadow.api.lang_model.shadow.type.PrimitiveLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.PrimitiveSupport;

import javax.lang.model.type.PrimitiveType;

public class PrimitiveImpl extends ShadowImpl<PrimitiveType> implements PrimitiveLangModel
{
   public PrimitiveImpl(LangModelContext context, PrimitiveType primitiveTypeMirror)
   {
      super(context, primitiveTypeMirror);
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).isSubtype(LangModelAdapter.particularType((ShadowLangModel) shadow), getMirror());
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).isAssignable(getMirror(), LangModelAdapter.particularType((ShadowLangModel) shadow));
   }

   @Override
   public ClassLangModel asBoxed()
   {
      return LangModelAdapter
                     .generalize(getApi(), LangModelAdapter.getTypes(getApi()).boxedClass(getMirror()).asType());
   }

   @Override
   public TypeKind getKind()
   {
      return switch (getMirror().getKind())
      {
         case BOOLEAN -> TypeKind.BOOLEAN;
         case BYTE -> TypeKind.BYTE;
         case SHORT -> TypeKind.SHORT;
         case INT -> TypeKind.INT;
         case LONG -> TypeKind.LONG;
         case CHAR -> TypeKind.CHAR;
         case FLOAT -> TypeKind.FLOAT;
         case DOUBLE -> TypeKind.DOUBLE;
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
