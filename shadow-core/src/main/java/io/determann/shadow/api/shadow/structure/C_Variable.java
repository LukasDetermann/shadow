package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.shadow.C_Annotationable;
import io.determann.shadow.api.shadow.C_ModuleEnclosed;
import io.determann.shadow.api.shadow.C_Nameable;
import io.determann.shadow.api.shadow.modifier.C_Modifiable;
import io.determann.shadow.api.shadow.type.C_Shadow;

/**
 * <ul>
 *    <li>{@link C_EnumConstant}</li>
 *    <li>{@link C_Field}</li>
 *    <li>{@link C_Parameter}</li>
 * </ul>
 */
public interface C_Variable extends C_Shadow,
                                    C_Nameable,
                                    C_Annotationable,
                                    C_Modifiable,
                                    C_ModuleEnclosed
{
}
