package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.*;

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
