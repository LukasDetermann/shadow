package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.Record;
import org.determann.shadow.api.shadow.*;

import java.util.Optional;

public interface DeclaredConverter
{
   Annotation toAnnotation();

   Optional<Annotation> toOptionalAnnotation();

   Enum toEnum();

   Optional<Enum> toOptionalEnum();

   Class toClass();

   Optional<Class> toOptionalClass();

   Interface toInterface();

   Optional<Interface> toOptionalInterface();

   Record toRecord();

   Optional<Record> toOptionalRecord();

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
