package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.R_Annotationable;
import io.determann.shadow.api.reflection.shadow.R_Nameable;
import io.determann.shadow.api.reflection.shadow.modifier.R_Modifiable;
import io.determann.shadow.api.reflection.shadow.type.R_Shadow;
import io.determann.shadow.api.shadow.structure.C_EnumConstant;
import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.structure.C_Variable;
import io.determann.shadow.api.shadow.type.C_Shadow;

/**
 * Can be converted using {@link Converter#convert(R_Variable)}
 * <ul>
 *    <li>{@link C_EnumConstant}</li>
 *    <li>{@link C_Field}</li>
 *    <li>{@link C_Parameter}</li>
 * </ul>
 */
public interface R_Variable extends C_Variable,
                                    R_Annotationable,
                                    R_Shadow,
                                    R_Nameable,
                                    R_ModuleEnclosed,
                                    R_Modifiable
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a shadow implements for example a
    * {@link java.util.Collection} {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    */
   boolean isSubtypeOf(C_Shadow shadow);

   /**
    * Equivalent to {@link #isSubtypeOf(C_Shadow)} except for primitives.
    * if one is a primitive and the other is not it tries to convert them
    */
   boolean isAssignableFrom(C_Shadow shadow);

   R_Shadow getType();

   R_Package getPackage();

   /**
    * The {@link Object} surrounding this {@link R_Variable}
    */
   Object getSurrounding();
}
