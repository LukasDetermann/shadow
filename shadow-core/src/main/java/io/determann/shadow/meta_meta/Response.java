package io.determann.shadow.meta_meta;

public sealed interface Response<T>
{
   public final class Unsupported<T> implements Response<T>
   {
   }

   public final class Result<T> implements Response<T>
   {
      private final T value;

      public Result(T value) {this.value = value;}

      public T getValue()
      {
         return value;
      }
   }
}
