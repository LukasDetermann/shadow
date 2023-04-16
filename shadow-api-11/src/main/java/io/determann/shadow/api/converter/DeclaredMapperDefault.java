package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.Annotation;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Interface;

/**
 * @see DeclaredConverter#mapper(DeclaredMapper)
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
}
