package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.modifier.Modifiable;

import javax.lang.model.element.RecordComponentElement;

public interface RecordComponent extends Shadow,
                                         Annotationable<RecordComponentElement>,
                                         Modifiable
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

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link RecordComponent}s this means for example {@code record MyRecord<T>(T t){}} -> {@code record MyRecord<T>(java.lang.Object t){}}
    */
   RecordComponent erasure();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
