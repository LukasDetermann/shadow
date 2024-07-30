package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.type.IntersectionLangModel;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.IntersectionSupport;

import javax.lang.model.type.IntersectionType;
import java.util.List;

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
      return IntersectionSupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return IntersectionSupport.equals(this, other);
   }

   @Override
   public String toString()
   {
      return IntersectionSupport.toString(this);
   }
}
