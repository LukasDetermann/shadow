package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.Annotation;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Interface;

/**
 * @see DeclaredConverter#mapper(DeclaredMapper)
 */
public interface DeclaredMapper<T>
{
   T annotationType(Annotation annotation);

   T enumType(Enum aEnum);

   T classType(Class aClass);

   T interfaceType(Interface aInterface);
}
