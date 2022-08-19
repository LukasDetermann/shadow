package org.determann.shadow.api.shadow;

import org.determann.shadow.api.Annotationable;

import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.type.TypeMirror;

public interface RecordComponent extends Shadow<TypeMirror>,
                                         Annotationable<RecordComponentElement>
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a shadow implements for example a
    * {@link java.util.Collection} {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclared("java.util.Collection").erasure())}
    */
   boolean isSubtypeOf(Shadow<? extends TypeMirror> shadow);

   /**
    * Equivalent to {@link #isSubtypeOf(Shadow)} except for primitives.
    * if one is a primitive and the other is not it tries to convert them
    */
   boolean isAssignableFrom(Shadow<? extends TypeMirror> shadow);

   /**
    * returns the record this is a port of
    */
   Record getRecord();

   Shadow<TypeMirror> getType();

   Method getGetter();

   Package getPackage();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
