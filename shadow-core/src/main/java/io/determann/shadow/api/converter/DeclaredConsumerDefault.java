package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.*;

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
