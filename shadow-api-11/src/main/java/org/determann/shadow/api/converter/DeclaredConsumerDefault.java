package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.Annotation;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.Interface;

/**
 * @see DeclaredConverter#consumer(DeclaredConsumer)
 */
public abstract class DeclaredConsumerDefault implements DeclaredConsumer
{
   @Override
   public void annotationType(Annotation annotation)
   {
   }

   @Override
   public void enumType(Enum aEnum)
   {
   }

   @Override
   public void classType(Class aClass)
   {
   }

   @Override
   public void interfaceType(Interface aInterface)
   {
   }
}
