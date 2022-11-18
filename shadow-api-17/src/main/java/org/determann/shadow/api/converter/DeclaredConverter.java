package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.Record;
import org.determann.shadow.api.shadow.*;

import java.util.Optional;

public interface DeclaredConverter
{
   Annotation toAnnotationOrThrow();

   Optional<Annotation> toAnnotation();

   Enum toEnumOrThrow();

   Optional<Enum> toEnum();

   Class toClassOrThrow();

   Optional<Class> toClass();

   Interface toInterfaceThrowOrThrow();

   Optional<Interface> toInterface();

   Record toRecordOrThrow();

   Optional<Record> toRecord();

   /**
    * consumes all leafs of {@link Declared}
    */
   void consumer(DeclaredConsumer adapter);

   /**
    * consumes all leafs of {@link Declared}
    */
   <T> T mapper(DeclaredMapper<T> mapper);

   Array asArray();

   Wildcard asExtendsWildcard();

   Wildcard asSuperWildcard();

   Primitive asUnboxed();
}
