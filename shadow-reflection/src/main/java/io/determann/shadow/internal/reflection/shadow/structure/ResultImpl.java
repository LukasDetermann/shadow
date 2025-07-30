package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.Provider;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.R_AnnotationUsage;
import io.determann.shadow.api.reflection.shadow.structure.R_Result;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.structure.C_Result;

import java.lang.reflect.AnnotatedType;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.Operations.RESULT_GET_TYPE;
import static io.determann.shadow.api.reflection.R_Adapter.IMPLEMENTATION;

public class ResultImpl
      implements R_Result
{
   private final AnnotatedType annotatedType;

   public ResultImpl(AnnotatedType annotatedType)
   {
      this.annotatedType = annotatedType;
   }

   @Override
   public List<R_AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getAnnotatedType().getAnnotations())
                   .map(R_Adapter::generalize)
                   .toList();
   }

   @Override
   public List<R_AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getAnnotatedType().getDeclaredAnnotations())
                   .map(R_Adapter::generalize)
                   .toList();
   }

   @Override
   public R_Type getType()
   {
      return R_Adapter.generalize(getAnnotatedType().getType());
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
      if (!(other instanceof C_Result otherReturn))
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
