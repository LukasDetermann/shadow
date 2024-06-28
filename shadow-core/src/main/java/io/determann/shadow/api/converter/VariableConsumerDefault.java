package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.structure.Parameter;

/**
 * @see VariableConverter#consume(VariableConsumer)
 */
public abstract class VariableConsumerDefault implements VariableConsumer
{
   @Override
   public void enumConstant(EnumConstant enumConstant)
   {
   }

   @Override
   public void field(Field field)
   {
   }

   @Override
   public void parameter(Parameter parameter)
   {
   }
}
