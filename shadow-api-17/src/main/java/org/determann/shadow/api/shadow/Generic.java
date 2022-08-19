package org.determann.shadow.api.shadow;

import org.determann.shadow.api.Annotationable;

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
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
