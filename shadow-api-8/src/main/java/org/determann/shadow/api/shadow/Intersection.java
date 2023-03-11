package org.determann.shadow.api.shadow;

import org.jetbrains.annotations.UnmodifiableView;

import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

/**
 * {@code T extends} <b> Collection & Serializable</b>{@code >}
 */
public interface Intersection extends Shadow<IntersectionType>
{
   /**
    *  Collection & Serializable -> Collection & Serializable[]
    */
   Array asArray();

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * The erasure of an IntersectionType is its first bound type
    * public class IntersectionExample<T extends Collection & Serializable>{} -> Collection
    * public class IntersectionExample<T extends Serializable & Collection>{} -> Serializable
    */
   Shadow<TypeMirror> erasure();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);

   /**
    * {@code T extends} <b> Collection & Serializable</b>{@code >}
    */
   @UnmodifiableView List<Shadow<TypeMirror>> getBounds();
}
