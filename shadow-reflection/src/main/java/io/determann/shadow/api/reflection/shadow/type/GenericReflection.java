package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.NameableReflection;
import io.determann.shadow.api.shadow.type.Generic;
import io.determann.shadow.api.shadow.type.Intersection;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.Optional;

/**
 * represents the generic parameter at a class, method, constructor etc.
 */
public interface GenericReflection extends Generic,
                                           ShadowReflection,
                                           NameableReflection
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
