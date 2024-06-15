package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.ModuleEnclosed;
import io.determann.shadow.api.Nameable;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.modifier.Modifiable;

/**
 * Can be converted using {@link Converter#convert(Variable)}
 * <ul>
 *    <li>{@link EnumConstant}</li>
 *    <li>{@link Field}</li>
 *    <li>{@link Parameter}</li>
 * </ul>
 */
public interface Variable extends Shadow,
                                  Nameable,
                                  Annotationable,
                                  Modifiable,
                                  ModuleEnclosed
{
}
