package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.shadow.C_Annotationable;
import io.determann.shadow.api.shadow.C_Documented;
import io.determann.shadow.api.shadow.C_ModuleEnclosed;
import io.determann.shadow.api.shadow.C_Nameable;
import io.determann.shadow.api.shadow.modifier.C_Modifiable;

/**
 * <ul>
 *    <li>{@link C_Constructor}</li>
 *    <li>{@link C_Method}</li>
 * </ul>
 */
public interface C_Executable extends C_Annotationable,
                                      C_Nameable,
                                      C_Modifiable,
                                      C_ModuleEnclosed,
                                      C_Documented
{
}
