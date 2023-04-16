package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.Annotation;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Interface;

/**
 * @see DeclaredConverter#consumer(DeclaredConsumer)
 */
public interface DeclaredConsumer
{
   void annotationType(Annotation annotation);

   void enumType(Enum aEnum);

   void classType(Class aClass);

   void interfaceType(Interface aInterface);
}
