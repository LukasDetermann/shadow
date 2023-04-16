package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.Constructor;
import io.determann.shadow.api.shadow.Method;

/**
 * @see ExecutableConverter#consumer(ExecutableConsumer)
 */
public abstract class ExecutableConsumerDefault implements ExecutableConsumer
{
   @Override
   public void constructor(Constructor constructor)
   {
   }

   @Override
   public void method(Method method)
   {
   }
}