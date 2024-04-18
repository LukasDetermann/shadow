package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Receiver;
import io.determann.shadow.api.shadow.Shadow;

import java.lang.reflect.AnnotatedType;
import java.util.Arrays;
import java.util.List;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

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
                   .map(ReflectionAdapter::generalize)
                   .toList();
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getAnnotatedType().getDeclaredAnnotations())
                   .map(ReflectionAdapter::generalize)
                   .toList();
   }

   @Override
   public Shadow getType()
   {
      return ReflectionAdapter.generalize(annotatedType.getType());
   }

   public AnnotatedType getAnnotatedType()
   {
      return annotatedType;
   }


   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
