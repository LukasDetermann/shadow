package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Array;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Primitive;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import java.util.Objects;

public class PrimitiveImpl extends ShadowImpl<PrimitiveType> implements Primitive
{
   public PrimitiveImpl(ShadowApi shadowApi, PrimitiveType primitiveTypeMirror)
   {
      super(shadowApi, primitiveTypeMirror);
   }

   @Override
   public boolean isSubtypeOf(Shadow<? extends TypeMirror> shadow)
   {
      return getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().isSubtype(shadow.getMirror(), getMirror());
   }

   @Override
   public boolean isAssignableFrom(Shadow<? extends TypeMirror> shadow)
   {
      return getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().isAssignable(getMirror(), shadow.getMirror());
   }

   @Override
   public Class asBoxed()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().boxedClass(getMirror()).asType());
   }

   @Override
   public Array asArray()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().getArrayType(getMirror()));
   }

   @Override
   public TypeKind getTypeKind()
   {
      switch (getMirror().getKind())
      {
         case BOOLEAN:
            return TypeKind.BOOLEAN;
         case BYTE:
            return TypeKind.BYTE;
         case SHORT:
            return TypeKind.SHORT;
         case INT:
            return TypeKind.INT;
         case LONG:
            return TypeKind.LONG;
         case CHAR:
            return TypeKind.CHAR;
         case FLOAT:
            return TypeKind.FLOAT;
         case DOUBLE:
            return TypeKind.DOUBLE;
         default:
            throw new IllegalStateException();
      }
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
      if (other == null || !getClass().equals(other.getClass()))
      {
         return false;
      }
      PrimitiveImpl otherPrimitive = (PrimitiveImpl) other;
      return Objects.equals(getTypeKind(), otherPrimitive.getTypeKind());
   }
}
