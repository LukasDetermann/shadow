package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.Constructor;
import org.determann.shadow.api.shadow.Executable;
import org.determann.shadow.api.shadow.Method;

import java.util.Optional;

public interface ExecutableConverter
{
   Constructor toConstructor();

   Optional<Constructor> toOptionalConstructor();

   Method toMethod();

   Optional<Method> toOptionalMethod();

   /**
    * consumes all leafs of {@link Executable}
    */
   void consumer(ExecutableConsumer adapter);

   /**
    * consumes all leafs of {@link Executable}
    */
   <T> T mapper(ExecutableMapper<T> mapper);
}
