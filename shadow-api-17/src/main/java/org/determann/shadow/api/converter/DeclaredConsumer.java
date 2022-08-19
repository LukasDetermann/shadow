package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.Record;
import org.determann.shadow.api.shadow.*;

/**
 * @see DeclaredConverter#consumer(DeclaredConsumer)
 */
public interface DeclaredConsumer
{
   void annotationType(Annotation annotation);

   void enumType(Enum aEnum);

   void classType(Class aClass);

   void interfaceType(Interface aInterface);

   void recordType(Record aRecord);
}
