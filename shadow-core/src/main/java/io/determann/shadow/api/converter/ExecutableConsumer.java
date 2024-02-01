package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.Constructor;
import io.determann.shadow.api.shadow.Method;

/**
 * @see ExecutableConverter#consume(ExecutableConsumer)
 */
public interface ExecutableConsumer
{
   void constructor(Constructor constructor);

   void method(Method method);
}
