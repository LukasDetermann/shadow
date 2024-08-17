package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.AnnotationableReflection;
import io.determann.shadow.api.reflection.shadow.NameableReflection;
import io.determann.shadow.api.shadow.type.Generic;
import io.determann.shadow.api.shadow.type.Intersection;

import java.util.Optional;

/**
 * represents the generic parameter at a class, method, constructor etc.
 */
public interface GenericReflection extends Generic,
                                           AnnotationableReflection,
                                           ShadowReflection,
                                           NameableReflection
{
   /**
    * @see Intersection
    */
   ShadowReflection getExtends();

   Optional<ShadowReflection> getSuper();

   /**
    * returns the class, method constructor etc. this is the generic for
    */
   Object getEnclosing();
}
