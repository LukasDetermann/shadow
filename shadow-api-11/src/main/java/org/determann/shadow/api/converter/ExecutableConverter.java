package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.Constructor;
import org.determann.shadow.api.shadow.Executable;
import org.determann.shadow.api.shadow.Method;

import java.util.Optional;

public interface ExecutableConverter
{
   Optional<Constructor> toConstructor();

   Optional<Method> toMethod();

   /**
    * consumes all leafs of {@link Executable}
    */
   void consumer(ExecutableConsumer adapter);

   /**
    * consumes all leafs of {@link Executable}
    */
   <T> T mapper(ExecutableMapper<T> mapper);
}
