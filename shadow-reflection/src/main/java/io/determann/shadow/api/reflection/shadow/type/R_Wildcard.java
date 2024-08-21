package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.api.shadow.type.C_Wildcard;

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
public interface R_Wildcard extends C_Wildcard,
                                    R_Shadow
{
   /**
    * {@snippet :
    *  List<? extends Number>//@highlight substring="? extends Number"
    *}
    */
   Optional<R_Shadow> getExtends();

   /**
    * {@snippet :
    *  List<? super Number>//@highlight substring="? super Number"
    *}
    */
   Optional<R_Shadow> getSuper();

   /**
    * {@snippet file = "WildcardTest.java" region = "Wildcard.contains"}
    */
   boolean contains(C_Shadow shadow);
}
