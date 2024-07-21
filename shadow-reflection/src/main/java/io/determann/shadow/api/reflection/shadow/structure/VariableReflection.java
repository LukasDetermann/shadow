package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.reflection.shadow.NameableReflection;
import io.determann.shadow.api.reflection.shadow.modifier.ModifiableReflection;
import io.determann.shadow.api.reflection.shadow.type.ShadowReflection;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.structure.*;
import io.determann.shadow.api.shadow.type.Shadow;

/**
 * Can be converted using {@link Converter#convert(VariableReflection)}
 * <ul>
 *    <li>{@link EnumConstant}</li>
 *    <li>{@link Field}</li>
 *    <li>{@link Parameter}</li>
 * </ul>
 */
public interface VariableReflection extends Variable,
                                            ShadowReflection,
                                            NameableReflection,
                                            ModuleEnclosedReflection,
                                            ModifiableReflection
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a shadow implements for example a
    * {@link java.util.Collection} {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    */
   boolean isSubtypeOf(Shadow shadow);

   /**
    * Equivalent to {@link #isSubtypeOf(Shadow)} except for primitives.
    * if one is a primitive and the other is not it tries to convert them
    */
   boolean isAssignableFrom(Shadow shadow);

   Shadow getType();

   Package getPackage();

   /**
    * The {@link Object} surrounding this {@link VariableReflection}
    */
   Object getSurrounding();
}
