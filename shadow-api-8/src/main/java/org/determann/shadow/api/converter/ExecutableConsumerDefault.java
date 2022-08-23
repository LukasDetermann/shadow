package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.Constructor;
import org.determann.shadow.api.shadow.Method;

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