package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.Constructor;
import org.determann.shadow.api.shadow.Method;

/**
 * @see ExecutableConverter#mapper(ExecutableMapper)
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
