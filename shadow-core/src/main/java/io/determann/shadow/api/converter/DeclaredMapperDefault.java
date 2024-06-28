package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.*;

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
