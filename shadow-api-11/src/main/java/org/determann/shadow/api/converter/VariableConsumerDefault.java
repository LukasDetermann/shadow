package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.EnumConstant;
import org.determann.shadow.api.shadow.Field;
import org.determann.shadow.api.shadow.Parameter;

/**
 * @see VariableConverter#consumer(VariableConsumer)
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
