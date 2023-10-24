package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.modifier.Modifiable;

/**
 * represents the generic parameter at a class, method, constructor etc.
 */
public interface Generic extends Shadow,
                                 Annotationable,
                                 Modifiable
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

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
