package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.implementation.support.api.shadow.type.GenericSupport;

import javax.lang.model.type.IntersectionType;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;

public class IntersectionImpl extends TypeImpl<IntersectionType> implements AP.Generic
{
   public IntersectionImpl(AP.Context context, IntersectionType intersectionType)
   {
      super(context, intersectionType);
   }

   @Override
   public AP.Type getBound()
   {
      return getBounds().getFirst();
   }

   @Override
   public List<AP.Type> getBounds()
   {
      return getMirror().getBounds().stream()
                        .map(typeMirror -> adapt(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<AP.Interface> getAdditionalBounds()
   {
      List<AP.Type> bounds = getBounds();
      if (bounds.size() <= 1)
      {
         return Collections.emptyList();
      }
      return bounds.stream().skip(1)
                   .map(AP.Interface.class::cast)
                   .toList();
   }

   @Override
   public Optional<AP.Type> getSuper()
   {
      return Optional.empty();
   }

   @Override
   public Object getEnclosing()
   {
      throw new IllegalStateException("can not get the enclosing Object for a generic backed by an intersection type");
   }

   @Override
   public AP.Generic erasure()
   {
      return (AP.Generic) adapt(getApi(), adapt(getApi()).toTypes().erasure(getMirror()));
   }

   @Override
   public List<AP.AnnotationUsage> getAnnotationUsages()
   {
      return Collections.emptyList();
   }

   @Override
   public List<AP.AnnotationUsage> getDirectAnnotationUsages()
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
