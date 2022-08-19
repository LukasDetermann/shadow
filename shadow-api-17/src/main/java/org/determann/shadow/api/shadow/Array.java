package org.determann.shadow.api.shadow;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

public interface Array extends Shadow<ArrayType>
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a shadow implements for example a
    * {@link java.util.Collection} {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclared("java.util.Collection").erasure())}
    */
   boolean isSubtypeOf(Shadow<? extends TypeMirror> shadow);

   /**
    * <b>String</b>{@code []}
    */
   Shadow<TypeMirror> getComponentType();

   /**
    * returns Object[] for declared arrays and an {@link Intersection} of {@code java.io.Serializable&java.lang.Cloneable}
    * for primitives
    */
   List<Shadow<TypeMirror>> getDirectSuperTypes();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
