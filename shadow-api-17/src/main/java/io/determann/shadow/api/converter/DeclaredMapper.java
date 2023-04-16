package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;

/**
 * @see DeclaredConverter#mapper(DeclaredMapper)
 */
public interface DeclaredMapper<T>
{
   T annotationType(Annotation annotation);

   T enumType(Enum aEnum);

   T classType(Class aClass);

   T interfaceType(Interface aInterface);

   T recordType(Record aRecord);
}
