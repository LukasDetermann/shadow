package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.Constructor;
import org.determann.shadow.api.shadow.Method;

/**
 * @see ExecutableConverter#consumer(ExecutableConsumer)
 */
public interface ExecutableConsumer
{
   void constructor(Constructor constructor);

   void method(Method method);
}
