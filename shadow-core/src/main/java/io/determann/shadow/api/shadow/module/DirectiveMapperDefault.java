package io.determann.shadow.api.shadow.module;

public abstract class DirectiveMapperDefault<T> implements DirectiveMapper<T>
{
   @Override
   public T exports(Exports exports)
   {
      return null;
   }

   @Override
   public T opens(Opens opens)
   {
      return null;
   }

   @Override
   public T provides(Provides provides)
   {
      return null;
   }

   @Override
   public T requires(Requires requires)
   {
      return null;
   }

   @Override
   public T uses(Uses uses)
   {
      return null;
   }
}
