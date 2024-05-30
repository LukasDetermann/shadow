package io.determann.shadow.api.reflection.query;

import io.determann.shadow.api.shadow.Shadow;

import java.util.Optional;

/**
 * {@snippet id = "test":
 *  List<? extends Number>//@highlight substring="? extends Number"
 *}
 * or
 * {@snippet :
 *  List<? super Number>//@highlight substring="? super Number"
 *}
 */
public interface WildcardReflection extends ShadowReflection
{
   /**
    * {@snippet :
    *  List<? extends Number>//@highlight substring="? extends Number"
    *}
    */
   Optional<Shadow> getExtends();

   /**
    * {@snippet :
    *  List<? super Number>//@highlight substring="? super Number"
    *}
    */
   Optional<Shadow> getSuper();

   /**
    * {@snippet file = "WildcardTest.java" region = "Wildcard.contains"}
    */
   boolean contains(Shadow shadow);
}
