package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.EnumConstant;
import org.determann.shadow.api.shadow.Field;
import org.determann.shadow.api.shadow.Parameter;

/**
 * @see VariableConverter#mapper(VariableMapper)
 */
public abstract class VariableMapperDefault<T> implements VariableMapper<T>
{
   @Override
   public T enumConstant(EnumConstant enumConstant)
   {
      return null;
   }

   @Override
   public T field(Field field)
   {
      return null;
   }

   @Override
   public T parameter(Parameter parameter)
   {
      return null;
   }
}