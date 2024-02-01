package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.ModuleEnclosed;
import io.determann.shadow.api.Nameable;

public interface RecordComponent extends Shadow,
                                         Nameable,
                                         Annotationable,
                                         ModuleEnclosed
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a shadow implements for example a
    * {@link java.util.Collection} {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    */
   boolean isSubtypeOf(Shadow shadow);

   /**
    * Equivalent to {@link #isSubtypeOf(Shadow)} except for primitives.
    * if one is a primitive and the other is not it tries to convert them
    */
   boolean isAssignableFrom(Shadow shadow);

   /**
    * returns the record this is a port of
    */
   Record getRecord();

   Shadow getType();

   Method getGetter();

   Package getPackage();
}
