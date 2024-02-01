package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;

/**
 * @see DeclaredConverter#consume(DeclaredConsumer)
 */
public interface DeclaredConsumer
{
   void annotationType(Annotation annotation);

   void enumType(Enum aEnum);

   void classType(Class aClass);

   void interfaceType(Interface aInterface);

   void recordType(Record aRecord);
}
