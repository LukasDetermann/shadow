package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.EnumConstant;
import org.determann.shadow.api.shadow.Field;
import org.determann.shadow.api.shadow.Parameter;

/**
 * @see VariableConverter#consumer(VariableConsumer)
 */
public interface VariableConsumer
{
   void enumConstant(EnumConstant enumConstant);

   void field(Field field);

   void parameter(Parameter parameter);
}