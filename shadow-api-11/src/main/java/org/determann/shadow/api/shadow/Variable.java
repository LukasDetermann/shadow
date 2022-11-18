package org.determann.shadow.api.shadow;

import org.determann.shadow.api.Annotationable;
import org.determann.shadow.api.ShadowApi;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Can be converted using {@link ShadowApi#convert(Variable)}
 * <ul>
 *    <li>{@link EnumConstant}</li>
 *    <li>{@link Field}</li>
 *    <li>{@link Parameter}</li>
 * </ul>
 */
public interface Variable<SURROUNDING extends Shadow<? extends TypeMirror>> extends Shadow<TypeMirror>,
                                                                          Annotationable<VariableElement>
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a shadow implements for example a
    * {@link java.util.Collection} {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    */
   boolean isSubtypeOf(Shadow<? extends TypeMirror> shadow);

   /**
    * Equivalent to {@link #isSubtypeOf(Shadow)} except for primitives.
    * if one is a primitive and the other is not it tries to convert them
    */
   boolean isAssignableFrom(Shadow<? extends TypeMirror> shadow);

   Shadow<TypeMirror> getType();

   Package getPackage();

   /**
    * The {@link SURROUNDING} surrounding this {@link Variable}
    */
   SURROUNDING getSurrounding();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
