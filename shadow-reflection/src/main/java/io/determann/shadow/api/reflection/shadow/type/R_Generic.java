package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.R_Annotationable;
import io.determann.shadow.api.reflection.shadow.R_Nameable;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Intersection;

import java.util.Optional;

/**
 * represents the generic parameter at a class, method, constructor etc.
 */
public non-sealed interface R_Generic

      extends C_Generic,
              R_Annotationable,
              R_Type,
              R_Nameable
{
   /**
    * @see C_Intersection
    */
   R_Type getExtends();

   Optional<R_Type> getSuper();

   /**
    * returns the class, method constructor etc. this is the generic for
    */
   Object getEnclosing();
}
