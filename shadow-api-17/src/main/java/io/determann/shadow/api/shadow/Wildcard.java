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
}
