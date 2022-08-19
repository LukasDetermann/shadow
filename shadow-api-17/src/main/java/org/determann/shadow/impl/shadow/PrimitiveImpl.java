package org.determann.shadow.impl.shadow;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.TypeKind;
import org.determann.shadow.api.shadow.Primitive;
import org.determann.shadow.api.shadow.Shadow;

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
      return getApi().getJdkApiContext().types().isSubtype(shadow.getMirror(), getMirror());
   }

   @Override
   public boolean isAssignableFrom(Shadow<? extends TypeMirror> shadow)
   {
      return getApi().getJdkApiContext().types().isAssignable(getMirror(), shadow.getMirror());
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
      if (other == null || !getClass().equals(other.getClass()))
      {
         return false;
      }
      PrimitiveImpl otherPrimitive = (PrimitiveImpl) other;
      return Objects.equals(getTypeKind(), otherPrimitive.getTypeKind());
   }
}
