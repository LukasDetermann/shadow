package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.Constructor;
import io.determann.shadow.api.shadow.Method;

/**
 * @see ExecutableConverter#map(ExecutableMapper)
 */
public abstract class ExecutableMapperDefault<T> implements ExecutableMapper<T>
{
   @Override
   public T constructor(Constructor constructor)
   {
      return null;
   }

   @Override
   public T method(Method method)
   {
      return null;
   }
}
