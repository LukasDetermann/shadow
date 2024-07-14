package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.structure.ReturnReflection;
import io.determann.shadow.api.shadow.Provider;
import io.determann.shadow.api.shadow.annotationusage.AnnotationUsage;
import io.determann.shadow.api.shadow.structure.Return;
import io.determann.shadow.api.shadow.type.Shadow;

import java.lang.reflect.AnnotatedType;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.shadow.Operations.RETURN_GET_TYPE;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class ReturnImpl implements ReturnReflection
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
      return ReflectionAdapter.generalize(getAnnotatedType().getType());
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
      if (!(other instanceof Return otherReturn))
      {
         return false;
      }
      return Provider.requestOrEmpty(otherReturn, RETURN_GET_TYPE).map(shadow -> Objects.equals(shadow, getType())).orElse(false);
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
