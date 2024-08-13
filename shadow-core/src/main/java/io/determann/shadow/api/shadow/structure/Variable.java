package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.shadow.Annotationable;
import io.determann.shadow.api.shadow.ModuleEnclosed;
import io.determann.shadow.api.shadow.Nameable;
import io.determann.shadow.api.shadow.modifier.Modifiable;
import io.determann.shadow.api.shadow.type.Shadow;

/**
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
