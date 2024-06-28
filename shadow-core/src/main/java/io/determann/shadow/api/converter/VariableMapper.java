package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.structure.Parameter;

/**
 * @see VariableConverter#map(VariableMapper)
 */
public interface VariableMapper<T>
{
   T enumConstant(EnumConstant enumConstant);

   T field(Field field);

   T parameter(Parameter parameter);
}
