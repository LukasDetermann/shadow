package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.shadow.Array;
import io.determann.shadow.api.shadow.Intersection;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.IntersectionType;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class IntersectionImpl extends ShadowImpl<IntersectionType> implements Intersection
{

   public IntersectionImpl(AnnotationProcessingContext annotationProcessingContext, IntersectionType intersectionType)
   {
      super(annotationProcessingContext, intersectionType);
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.INTERSECTION;
   }

   @Override
   public List<Shadow> getBounds()
   {
      return getMirror().getBounds().stream()
                        .map(typeMirror -> MirrorAdapter.<Shadow>getShadow(getApi(), typeMirror))
                        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public Array asArray()
   {
      return MirrorAdapter.getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().getArrayType(getMirror()));
   }

   @Override
   public Shadow erasure()
   {
      return MirrorAdapter.getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().erasure(getMirror()));
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getBounds());
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
      IntersectionImpl otherIntersection = (IntersectionImpl) other;
      return Objects.equals(getBounds(), otherIntersection.getBounds());
   }
}
