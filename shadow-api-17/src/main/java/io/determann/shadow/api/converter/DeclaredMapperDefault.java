package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;

/**
 * @see DeclaredConverter#map(DeclaredMapper)
 */
public abstract class DeclaredMapperDefault<T> implements DeclaredMapper<T>
{
   @Override
   public T annotationType(Annotation annotation)
   {
      return null;
   }

   @Override
   public T enumType(Enum aEnum)
   {
      return null;
   }

   @Override
   public T classType(Class aClass)
   {
      return null;
   }

   @Override
   public T interfaceType(Interface aInterface)
   {
      return null;
   }

   @Override
   public T recordType(Record aRecord)
   {
      return null;
   }
}
