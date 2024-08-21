package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.shadow.LM_Annotationable;
import io.determann.shadow.api.lang_model.shadow.LM_Nameable;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Intersection;

import java.util.Optional;

/**
 * represents the generic parameter at a class, method, constructor etc.
 */
public interface LM_Generic extends C_Generic,
                                    LM_Annotationable,
                                    LM_Shadow,
                                    LM_Nameable
{
   /**
    * @see C_Intersection
    */
   LM_Shadow getExtends();

   Optional<LM_Shadow> getSuper();

   /**
    * returns the class, method constructor etc. this is the generic for
    */
   Object getEnclosing();
}
