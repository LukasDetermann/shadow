package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;

/**
 * @see DeclaredConverter#consume(DeclaredConsumer)
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

   @Override
   public void recordType(Record aRecord)
   {
   }
}
