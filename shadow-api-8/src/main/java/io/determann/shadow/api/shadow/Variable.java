package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.Nameable;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.modifier.Modifiable;

/**
 * Can be converted using {@link Converter#convert(Variable)}
 * <ul>
 *    <li>{@link EnumConstant}</li>
 *    <li>{@link Field}</li>
 *    <li>{@link Parameter}</li>
 * </ul>
 */
public interface Variable<SURROUNDING extends Shadow> extends Shadow,
                                                              Nameable,
                                                              Annotationable,
                                                              Modifiable
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
    * The {@link SURROUNDING} surrounding this {@link Variable}
    */
   SURROUNDING getSurrounding();
}
