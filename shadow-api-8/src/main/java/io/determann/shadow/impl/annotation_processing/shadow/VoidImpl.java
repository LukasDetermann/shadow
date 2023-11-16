package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.shadow.Void;

import javax.lang.model.type.NoType;
import java.util.Objects;

public class VoidImpl extends ShadowImpl<NoType> implements Void
{
   public VoidImpl(AnnotationProcessingContext annotationProcessingContext, NoType typeMirror)
   {
      super(annotationProcessingContext, typeMirror);
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.VOID;
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(getTypeKind());
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Void;
   }
}
