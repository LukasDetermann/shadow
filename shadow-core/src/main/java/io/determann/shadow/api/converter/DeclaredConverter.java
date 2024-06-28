package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.*;

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

   Record toRecordOrThrow();

   Optional<Record> toRecord();

   /**
    * consumes all leafs of {@link Declared}
    */
   void consume(DeclaredConsumer adapter);

   /**
    * consumes all leafs of {@link Declared}
    */
   <T> T map(DeclaredMapper<T> mapper);
}
