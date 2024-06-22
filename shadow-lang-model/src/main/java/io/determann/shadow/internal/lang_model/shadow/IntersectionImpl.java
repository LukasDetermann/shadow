package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.query.IntersectionLangModel;
import io.determann.shadow.api.shadow.Intersection;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.IntersectionType;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.meta_meta.Operations.INTERSECTION_GET_BOUNDS;
import static io.determann.shadow.meta_meta.Provider.request;

public class IntersectionImpl extends ShadowImpl<IntersectionType> implements IntersectionLangModel
{

   public IntersectionImpl(LangModelContext context, IntersectionType intersectionType)
   {
      super(context, intersectionType);
   }

   @Override
   public TypeKind getKind()
   {
      return TypeKind.INTERSECTION;
   }

   @Override
   public List<Shadow> getBounds()
   {
      return getMirror().getBounds().stream()
                        .map(typeMirror -> LangModelAdapter.<Shadow>generalize(getApi(), typeMirror))
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
      return request(otherIntersection, INTERSECTION_GET_BOUNDS).map(shadow -> Objects.equals(shadow, getBounds())).orElse(false);
   }
}
