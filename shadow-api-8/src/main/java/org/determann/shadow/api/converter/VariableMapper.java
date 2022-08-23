package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.EnumConstant;
import org.determann.shadow.api.shadow.Field;
import org.determann.shadow.api.shadow.Parameter;

/**
 * @see VariableConverter#mapper(VariableMapper)
 */
public interface VariableMapper<T>
{
   T enumConstant(EnumConstant enumConstant);

   T field(Field field);

   T parameter(Parameter parameter);
}
