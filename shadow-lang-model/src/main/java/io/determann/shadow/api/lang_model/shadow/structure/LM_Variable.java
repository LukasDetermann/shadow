package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.LM_Annotationable;
import io.determann.shadow.api.lang_model.shadow.LM_Documented;
import io.determann.shadow.api.lang_model.shadow.LM_ModuleEnclosed;
import io.determann.shadow.api.lang_model.shadow.LM_Nameable;
import io.determann.shadow.api.lang_model.shadow.modifier.LM_Modifiable;
import io.determann.shadow.api.lang_model.shadow.type.LM_Shadow;
import io.determann.shadow.api.shadow.structure.C_EnumConstant;
import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.structure.C_Variable;
import io.determann.shadow.api.shadow.type.C_Shadow;

/**
 * Can be converted using {@link Converter#convert(LM_Variable)}
 * <ul>
 *    <li>{@link C_EnumConstant}</li>
 *    <li>{@link C_Field}</li>
 *    <li>{@link C_Parameter}</li>
 * </ul>
 */
public sealed interface LM_Variable

      extends C_Variable,
              LM_Annotationable,
              LM_Documented,
              LM_Nameable,
              LM_ModuleEnclosed,
              LM_Modifiable

      permits LM_EnumConstant,
              LM_Field,
              LM_Parameter
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

   LM_Shadow getType();

   LM_Package getPackage();

   /**
    * The {@link Object} surrounding this {@link LM_Variable}
    */
   Object getSurrounding();
}