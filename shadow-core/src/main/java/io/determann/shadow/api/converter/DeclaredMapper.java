package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.*;

/**
 * @see DeclaredConverter#map(DeclaredMapper)
 */
public interface DeclaredMapper<T>
{
   T annotationType(Annotation annotation);

   T enumType(Enum aEnum);

   T classType(Class aClass);

   T interfaceType(Interface aInterface);

   T recordType(Record aRecord);
}
