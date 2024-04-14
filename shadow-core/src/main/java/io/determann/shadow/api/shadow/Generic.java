package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.Nameable;

import java.util.Optional;

/**
 * represents the generic parameter at a class, method, constructor etc.
 */
public interface Generic extends Shadow,
                                 Nameable,
                                 Annotationable
{
   /**
    * @see Intersection
    */
   Shadow getExtends();

   Optional<Shadow> getSuper();

   /**
    * returns the class, method constructor etc. this is the generic for
    */
   Object getEnclosing();
}
