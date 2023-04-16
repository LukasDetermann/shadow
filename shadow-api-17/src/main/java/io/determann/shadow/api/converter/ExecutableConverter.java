package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.Constructor;
import io.determann.shadow.api.shadow.Executable;
import io.determann.shadow.api.shadow.Method;

import java.util.Optional;

public interface ExecutableConverter
{
   Constructor toConstructorOrThrow();

   Optional<Constructor> toConstructor();

   Method toMethodOrThrow();

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
