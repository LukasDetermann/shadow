package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Receiver;
import io.determann.shadow.api.shadow.Shadow;

import java.lang.reflect.AnnotatedType;
import java.util.Arrays;
import java.util.List;

public class ReceiverImpl implements Receiver
{
   private final AnnotatedType annotatedType;

   public ReceiverImpl(AnnotatedType annotatedType)
   {

      this.annotatedType = annotatedType;
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getAnnotatedType().getAnnotations())
                   .map(ReflectionAdapter::getAnnotationUsage)
                   .toList();
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getAnnotatedType().getDeclaredAnnotations())
                   .map(ReflectionAdapter::getAnnotationUsage)
                   .toList();
   }

   @Override
   public Shadow getType()
   {
      return ReflectionAdapter.getShadow(annotatedType.getType());
   }

   public AnnotatedType getAnnotatedType()
   {
      return annotatedType;
   }
}
