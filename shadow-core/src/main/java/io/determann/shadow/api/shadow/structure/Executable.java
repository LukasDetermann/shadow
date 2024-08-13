package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.shadow.Annotationable;
import io.determann.shadow.api.shadow.Documented;
import io.determann.shadow.api.shadow.ModuleEnclosed;
import io.determann.shadow.api.shadow.Nameable;
import io.determann.shadow.api.shadow.modifier.Modifiable;

/**
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
