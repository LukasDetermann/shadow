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

import static io.determann.shadow.api.query.Operations.RECEIVER_GET_TYPE;
import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;

public class ReceiverImpl implements R.Receiver
{
   private final AnnotatedType annotatedType;

   public ReceiverImpl(AnnotatedType annotatedType)
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
      return Adapter.generalize(annotatedType.getType());
   }

   public AnnotatedType getAnnotatedType()
   {
      return annotatedType;
   }


   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(getType());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof C.Receiver otherReceiver))
      {
         return false;
      }
      return Provider.requestOrEmpty(otherReceiver, RECEIVER_GET_TYPE).map(type -> Objects.equals(type, getType())).orElse(false);
   }
}
