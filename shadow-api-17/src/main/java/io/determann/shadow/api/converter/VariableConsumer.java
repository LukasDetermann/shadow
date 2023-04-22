package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.EnumConstant;
import io.determann.shadow.api.shadow.Field;
import io.determann.shadow.api.shadow.Parameter;

/**
 * @see VariableConverter#consume(VariableConsumer)
 */
public interface VariableConsumer
{
   void enumConstant(EnumConstant enumConstant);

   void field(Field field);

   void parameter(Parameter parameter);
}