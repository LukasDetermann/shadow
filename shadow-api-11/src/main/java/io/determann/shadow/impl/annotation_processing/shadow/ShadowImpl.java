package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.impl.annotation_processing.ApiHolder;

import javax.lang.model.type.TypeMirror;

public abstract class ShadowImpl<MIRROR extends TypeMirror> implements Shadow,
                                                                       ApiHolder
{
   private final AnnotationProcessingContext annotationProcessingContext;

   private final MIRROR typeMirror;

   protected ShadowImpl(AnnotationProcessingContext annotationProcessingContext, MIRROR typeMirror)
   {
      this.annotationProcessingContext = annotationProcessingContext;
      this.typeMirror = typeMirror;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().isSameType(getMirror(), MirrorAdapter.getType(shadow));
   }

   public MIRROR getMirror()
   {
      return typeMirror;
   }

   @Override
   public AnnotationProcessingContext getApi()
   {
      return annotationProcessingContext;
   }

   @Override
   public String toString()
   {
      return getMirror().toString();
   }
}
