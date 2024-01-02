package io.determann.shadow.impl.reflection.shadow;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Return;
import io.determann.shadow.api.shadow.Shadow;

import java.lang.reflect.AnnotatedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class ReturnImpl implements Return
{
   private final AnnotatedType annotatedType;

   public ReturnImpl(AnnotatedType annotatedType)
   {
      this.annotatedType = annotatedType;
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getAnnotatedType().getAnnotations())
                   .map(ReflectionAdapter::getAnnotationUsage)
                   .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getAnnotatedType().getDeclaredAnnotations())
                   .map(ReflectionAdapter::getAnnotationUsage)
                   .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public Shadow getType()
   {
      return ReflectionAdapter.getShadow(getAnnotatedType().getType());
   }

   public AnnotatedType getAnnotatedType()
   {
      return annotatedType;
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Return))
      {
         return false;
      }
      Return otherReturn = ((Return) other);
      return Objects.equals(getType(), otherReturn.getType());
   }
}
