package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;

/**
 * represents the generic parameter at a class, method, constructor etc.
 */
public interface Generic extends Shadow,
                                 Annotationable
{
   /**
    * @see Intersection
    */
   Shadow getExtends();

   Shadow getSuper();

   /**
    * returns the class, method constructor etc. this is the generic for
    */
   Shadow getEnclosing();

   Package getPackage();
}
