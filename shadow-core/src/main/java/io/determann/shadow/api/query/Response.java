package io.determann.shadow.api.query;

import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;

public sealed interface Response<T>
{
   record Result<T>(@UnknownNullability T value) implements Response<T> {}

   /**
    * Equivalent to Optional.empty to prevent to much nesting
    */
   final class Empty<T> implements Response<T>
   {
      @Override
      public int hashCode()
      {
         return Objects.hash(Empty.class);
      }

      @Override
      public boolean equals(Object obj)
      {
         return obj instanceof Empty<?>;
      }

      @Override
      public String toString()
      {
         return getClass().getName();
      }
   }

   final class Unsupported<T> implements Response<T>
   {
      @Override
      public int hashCode()
      {
         return Objects.hash(Unsupported.class);
      }

      @Override
      public boolean equals(Object obj)
      {
         return obj instanceof Unsupported<?>;
      }

      @Override
      public String toString()
      {
         return getClass().getName();
      }
   }
}
