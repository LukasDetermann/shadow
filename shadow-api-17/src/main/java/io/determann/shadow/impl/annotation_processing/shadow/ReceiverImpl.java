package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Receiver;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.TypeMirror;
import java.util.List;

public class ReceiverImpl implements Receiver
{
   private final AnnotationProcessingContext api;
   private final TypeMirror typeMirror;

   public ReceiverImpl(AnnotationProcessingContext api, TypeMirror typeMirror)
   {
      this.api = api;
      this.typeMirror = typeMirror;
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return getDirectAnnotationUsages();
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return MirrorAdapter.getAnnotationUsages(api, getTypeMirror().getAnnotationMirrors());
   }

   @Override
   public Shadow getType()
   {
      return MirrorAdapter.getShadow(api, getTypeMirror());
   }

   public TypeMirror getTypeMirror()
   {
      return typeMirror;
   }
}
