package io.determann.shadow.api.shadow;

import java.util.Optional;

/**
 * {@code List<}<b>? extends Number</b>{@code >}
 */
public interface Wildcard extends Shadow
{
   /**
    * {@code List<}<b>? extends Number</b>{@code >}
    */
   Optional<Shadow> getExtends();

   /**
    * {@code List<}<b>? super Number</b>{@code >}
    */
   Optional<Shadow> getSuper();

   /**
    * <pre>{@code
    *    shadowApi.getClassOrThrow("java.lang.Number")
    *             .asExtendsWildcard()
    *             .contains(shadowApi.getDeclaredOrThrow("java.lang.Long"));
    * }</pre>
    */
   boolean contains(Shadow shadow);

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link Wildcard}s this means for example {@code ? extends java.lang.Number} -&gt; {@code java.lang.Number}
    */
   Shadow erasure();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
