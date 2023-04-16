package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;

import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

/**
 * represents the generic parameter at a class, method, constructor etc.
 */
public interface Generic extends Shadow<TypeVariable>,
                                 Annotationable<TypeParameterElement>
{
   /**
    * @see Intersection
    */
   Shadow<TypeMirror> getExtends();

   /**
    * There is no way to explicitly declare super in a generic. Mostly relevant in the context of {@link Class#interpolateGenerics()}
    */
   Shadow<TypeMirror> getSuper();

   /**
    * returns the class, method constructor etc. this is the generic for
    */
   Shadow<TypeMirror> getEnclosing();

   Package getPackage();

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link Generic}s this means for example {@code T extends Number} -> {@code Number}
    */
   Shadow<TypeMirror> erasure();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}