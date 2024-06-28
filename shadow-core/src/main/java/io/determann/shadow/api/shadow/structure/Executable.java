package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.shadow.Annotationable;
import io.determann.shadow.api.shadow.Documented;
import io.determann.shadow.api.shadow.ModuleEnclosed;
import io.determann.shadow.api.shadow.Nameable;
import io.determann.shadow.api.shadow.modifier.Modifiable;
import io.determann.shadow.api.shadow.type.Declared;

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
