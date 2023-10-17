package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.shadow.Null;

import javax.lang.model.type.NullType;
import java.util.Objects;

public class NullImpl extends ShadowImpl<NullType> implements Null
{
   public NullImpl(AnnotationProcessingContext annotationProcessingContext, NullType nullType)
   {
      super(annotationProcessingContext, nullType);
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.NULL;
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(getTypeKind());
   }

   @Override
   public boolean equals(Object other)
   {
      return other != null && getClass().equals(other.getClass());
   }
}
