package io.determann.shadow.impl.lang_model.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Primitive;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.PrimitiveType;
import java.util.Objects;

public class PrimitiveImpl extends ShadowImpl<PrimitiveType> implements Primitive
{
   public PrimitiveImpl(LangModelContext context, PrimitiveType primitiveTypeMirror)
   {
      super(context, primitiveTypeMirror);
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).isSubtype(LangModelAdapter.getType(shadow), getMirror());
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).isAssignable(getMirror(), LangModelAdapter.getType(shadow));
   }

   @Override
   public Class asBoxed()
   {
      return LangModelAdapter
                     .getShadow(getApi(), LangModelAdapter.getTypes(getApi()).boxedClass(getMirror()).asType());
   }

   @Override
   public TypeKind getTypeKind()
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
   public int hashCode()
   {
      return Objects.hashCode(getTypeKind());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Primitive otherPrimitive))
      {
         return false;
      }
      return Objects.equals(getTypeKind(), otherPrimitive.getTypeKind());
   }
}
