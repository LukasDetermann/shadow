package io.determann.shadow.impl.lang_model.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.MirrorAdapter;
import io.determann.shadow.api.shadow.Intersection;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.IntersectionType;
import java.util.List;
import java.util.Objects;

public class IntersectionImpl extends ShadowImpl<IntersectionType> implements Intersection
{

   public IntersectionImpl(LangModelContext context, IntersectionType intersectionType)
   {
      super(context, intersectionType);
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
                        .toList();
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
      if (!(other instanceof Intersection otherIntersection))
      {
         return false;
      }
      return Objects.equals(getBounds(), otherIntersection.getBounds());
   }
}
