package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.Documented;
import io.determann.shadow.api.ModuleEnclosed;
import io.determann.shadow.api.Nameable;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.modifier.Modifiable;

/**
 * any code block. Can be converted into the following using {@link Converter#convert(Declared)}
 *
 * <ul>
 *    <li>{@link Constructor}</li>
 *    <li>{@link Method}</li>
 * </ul>
 */
public interface Executable extends Annotationable,
                                    Nameable,
                                    Modifiable,
                                    ModuleEnclosed,
                                    Documented
{
}
