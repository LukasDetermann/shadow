package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.Constructor;
import org.determann.shadow.api.shadow.Method;

/**
 * @see ExecutableConverter#mapper(ExecutableMapper)
 */
public interface ExecutableMapper<T>
{
   T constructor(Constructor constructor);

   T method(Method method);
}
