package io.determann.shadow.api.shadow;

import java.util.List;

/**
 * {@code T extends} <b> Collection &amp; Serializable</b>{@code >}
 */
public interface Intersection extends Shadow
{
   /**
    * {@code Collection & Serializable} -&gt;  {@code Collection & Serializable[]}
    */
   Array asArray();

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <pre>{@code
    * The erasure of an IntersectionType is its first bound type
    * public class IntersectionExample<T extends Collection & Serializable>{} -> Collection
    * public class IntersectionExample<T extends Serializable & Collection>{} -> Serializable
    * }</pre>
    */
   Shadow erasure();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);

   /**
    * {@code T extends} <b> Collection &amp; Serializable</b>{@code >}
    */
   List<Shadow> getBounds();
}
