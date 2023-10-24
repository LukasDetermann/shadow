package io.determann.shadow.api.shadow;

import java.util.List;

public interface Array extends Shadow
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a shadow implements for example a
    * {@link java.util.Collection} {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    */
   boolean isSubtypeOf(Shadow shadow);

   /**
    * <b>String</b>{@code []}
    */
   Shadow getComponentType();

   /**
    * returns Object[] for declared arrays and an {@link Intersection} of {@code java.io.Serializable&java.lang.Cloneable}
    * for primitives
    */
   List<Shadow> getDirectSuperTypes();

   Wildcard asExtendsWildcard();

   Wildcard asSuperWildcard();

   /**
    * String[] -&gt; String[][]
    */
   Array asArray();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
