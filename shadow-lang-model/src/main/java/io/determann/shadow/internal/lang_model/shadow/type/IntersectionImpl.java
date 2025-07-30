package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;
import io.determann.shadow.implementation.support.api.shadow.type.GenericSupport;

import javax.lang.model.type.IntersectionType;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.lang_model.adapter.Adapters.adapt;

public class IntersectionImpl extends TypeImpl<IntersectionType> implements LM.Generic
{
   public IntersectionImpl(LM.Context context, IntersectionType intersectionType)
   {
      super(context, intersectionType);
   }

   @Override
   public LM.Type getBound()
   {
      return getBounds().getFirst();
   }

   @Override
   public List<LM.Type> getBounds()
   {
      return getMirror().getBounds().stream()
                        .map(typeMirror -> adapt(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<LM.Interface> getAdditionalBounds()
   {
      List<LM.Type> bounds = getBounds();
      if (bounds.size() <= 1)
      {
         return Collections.emptyList();
      }
      return bounds.stream().skip(1)
                   .map(LM.Interface.class::cast)
                   .toList();
   }

   @Override
   public Optional<LM.Type> getSuper()
   {
      return Optional.empty();
   }

   @Override
   public Object getEnclosing()
   {
      throw new IllegalStateException("can not get the enclosing Object for a generic backed by an intersection type");
   }

   @Override
   public LM.Generic erasure()
   {
      return (LM.Generic) Adapters.adapt(getApi(), adapt(getApi()).toTypes().erasure(getMirror()));
   }

   @Override
   public List<LM.AnnotationUsage> getAnnotationUsages()
   {
      return Collections.emptyList();
   }

   @Override
   public List<LM.AnnotationUsage> getDirectAnnotationUsages()
   {
      return Collections.emptyList();
   }

   @Override
   public String getName()
   {
      return "";
   }

   @Override
   public boolean equals(Object obj)
   {
      return GenericSupport.equals(this, obj);
   }

   @Override
   public int hashCode()
   {
      return GenericSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return GenericSupport.toString(this);
   }
}
