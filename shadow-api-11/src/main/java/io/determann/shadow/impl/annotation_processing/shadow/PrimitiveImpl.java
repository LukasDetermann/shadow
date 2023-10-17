package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.shadow.Array;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Primitive;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.PrimitiveType;
import java.util.Objects;

public class PrimitiveImpl extends ShadowImpl<PrimitiveType> implements Primitive
{
   public PrimitiveImpl(AnnotationProcessingContext annotationProcessingContext, PrimitiveType primitiveTypeMirror)
   {
      super(annotationProcessingContext, primitiveTypeMirror);
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      return MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().isSubtype(MirrorAdapter.getType(shadow), getMirror());
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().isAssignable(getMirror(), MirrorAdapter.getType(shadow));
   }

   @Override
   public Class asBoxed()
   {
      return MirrorAdapter
                     .getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().boxedClass(getMirror()).asType());
   }

   @Override
   public Array asArray()
   {
      return MirrorAdapter.getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().getArrayType(getMirror()));
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
