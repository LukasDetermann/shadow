package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.Constructor;
import io.determann.shadow.api.shadow.Method;

/**
 * @see ExecutableConverter#mapper(ExecutableMapper)
 */
public interface ExecutableMapper<T>
{
   T constructor(Constructor constructor);

   T method(Method method);
}
