package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.*;

import java.util.Optional;

public interface DeclaredConverter
{
   Annotation toAnnotationOrThrow();

   Optional<Annotation> toAnnotation();

   Enum toEnumOrThrow();

   Optional<Enum> toEnum();

   Class toClassOrThrow();

   Optional<Class> toClass();

   Interface toInterfaceOrThrow();

   Optional<Interface> toInterface();

   /**
    * consumes all leafs of {@link Declared}
    */
   void consumer(DeclaredConsumer adapter);

   /**
    * consumes all leafs of {@link Declared}
    */
   <T> T mapper(DeclaredMapper<T> mapper);
}
