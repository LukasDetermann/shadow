package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.structure.Parameter;

/**
 * @see VariableConverter#map(VariableMapper)
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