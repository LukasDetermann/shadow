package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.shadow.*;
import io.determann.shadow.api.shadow.modifier.AccessModifiable;
import io.determann.shadow.api.shadow.modifier.StrictfpModifiable;

/**
 * Anything that can be a file.
 * <ul>
 *    <li>{@link Annotation}</li>
 *    <li>{@link Class}</li>
 *    <li>{@link Enum}</li>
 *    <li>{@link Interface}</li>
 *    <li>{@link Record}</li>
 * </ul>
 */
public interface Declared extends Shadow,
                                  Annotationable,
                                  AccessModifiable,
                                  StrictfpModifiable,
                                  Nameable,
                                  QualifiedNameable,
                                  ModuleEnclosed,
                                  Documented
{
}
