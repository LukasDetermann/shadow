package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.query.Provider;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;

import java.lang.reflect.AnnotatedType;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.query.Operations.RESULT_GET_TYPE;
import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;

public class ResultImpl
      implements R.Result
{
   private final AnnotatedType annotatedType;

   public ResultImpl(AnnotatedType annotatedType)
   {
      this.annotatedType = annotatedType;
   }

   @Override
   public List<R.AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getAnnotatedType().getAnnotations())
                   .map(Adapter::generalize)
                   .toList();
   }

   @Override
   public List<R.AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getAnnotatedType().getDeclaredAnnotations())
                   .map(Adapter::generalize)
                   .toList();
   }

   @Override
   public R.Type getType()
   {
      return Adapter.generalize(getAnnotatedType().getType());
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
      if (!(other instanceof C.Result otherReturn))
      {
         return false;
      }
      return Provider.requestOrEmpty(otherReturn, RESULT_GET_TYPE).map(type -> Objects.equals(type, getType())).orElse(false);
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getType());
   }

   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}
