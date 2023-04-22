package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.EnumConstant;
import io.determann.shadow.api.shadow.Field;
import io.determann.shadow.api.shadow.Parameter;

/**
 * @see VariableConverter#map(VariableMapper)
 */
public interface VariableMapper<T>
{
   T enumConstant(EnumConstant enumConstant);

   T field(Field field);

   T parameter(Parameter parameter);
}
