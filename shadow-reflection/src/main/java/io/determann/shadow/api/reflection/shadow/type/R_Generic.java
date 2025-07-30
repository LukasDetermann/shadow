package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.R_Annotationable;
import io.determann.shadow.api.reflection.shadow.R_Nameable;
import io.determann.shadow.api.shadow.type.C_Generic;

import java.util.List;

/**
 * represents the generic parameter at a class, method, constructor etc.
 */
public non-sealed interface R_Generic

      extends C_Generic,
              R_Annotationable,
              R_ReferenceType,
              R_Nameable
{
   R_Type getBound();

   List<R_Type> getBounds();

   List<R_Interface> getAdditionalBounds();

   /**
    * returns the class, method constructor etc. this is the generic for
    */
   Object getEnclosing();
}
