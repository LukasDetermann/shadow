package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.shadow.*;
import io.determann.shadow.api.shadow.modifier.C_AccessModifiable;
import io.determann.shadow.api.shadow.modifier.C_StrictfpModifiable;

/**
 * Anything that can be a file.
 * <ul>
 *    <li>{@link C_Annotation}</li>
 *    <li>{@link C_Class}</li>
 *    <li>{@link C_Enum}</li>
 *    <li>{@link C_Interface}</li>
 *    <li>{@link C_Record}</li>
 * </ul>
 */
public interface C_Declared extends C_Type,
                                    C_Annotationable,
                                    C_AccessModifiable,
                                    C_StrictfpModifiable,
                                    C_Nameable,
                                    C_QualifiedNameable,
                                    C_ModuleEnclosed,
                                    C_Documented
{
}
