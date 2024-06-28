package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.structure.Parameter;

/**
 * @see VariableConverter#consume(VariableConsumer)
 */
public interface VariableConsumer
{
   void enumConstant(EnumConstant enumConstant);

   void field(Field field);

   void parameter(Parameter parameter);
}