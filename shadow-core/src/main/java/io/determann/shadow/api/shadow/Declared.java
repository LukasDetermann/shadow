package io.determann.shadow.api.shadow;

import io.determann.shadow.api.*;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.modifier.AccessModifiable;
import io.determann.shadow.api.modifier.StrictfpModifiable;

/**
 * Anything that can be a file. Can be converted into the following using {@link Converter#convert(Declared)}
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
