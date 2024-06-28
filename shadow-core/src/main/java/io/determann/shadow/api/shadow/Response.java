package io.determann.shadow.api.shadow;

public sealed interface Response<T>
{
   /**
    * Equivalent to Optional.empty to prevent to much nesting
    */
   public final class Empty<T> implements Response<T>
   {
   }

   public final class Unsupported<T> implements Response<T>
   {
   }

   public record Result<T>(T value) implements Response<T>
   {
   }
}
