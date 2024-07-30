package io.determann.shadow.api.shadow;

import java.util.Objects;

public sealed interface Response<T>
{
   public record Result<T>(T value) implements Response<T>
   {
   }

   /**
    * Equivalent to Optional.empty to prevent to much nesting
    */
   public final class Empty<T> implements Response<T>
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

   public final class Unsupported<T> implements Response<T>
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
