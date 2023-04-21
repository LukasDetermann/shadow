package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Array;
import io.determann.shadow.api.shadow.Intersection;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toUnmodifiableList;

public class IntersectionImpl extends ShadowImpl<IntersectionType> implements Intersection
{

   public IntersectionImpl(ShadowApi shadowApi, IntersectionType intersectionType)
   {
      super(shadowApi, intersectionType);
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.INTERSECTION;
   }

   @Override
   public List<Shadow<TypeMirror>> getBounds()
   {
      return getMirror().getBounds().stream()
                        .map(typeMirror -> getApi().getShadowFactory().<Shadow<TypeMirror>>shadowFromType(typeMirror))
                        .collect(toUnmodifiableList());
   }

   @Override
   public Array asArray()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().processingEnv().getTypeUtils().getArrayType(getMirror()));
   }

   @Override
   public Shadow<TypeMirror> erasure()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().processingEnv().getTypeUtils().erasure(getMirror()));
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
