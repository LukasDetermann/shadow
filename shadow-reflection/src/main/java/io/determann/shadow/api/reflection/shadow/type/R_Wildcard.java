package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.shadow.type.C_Type;
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
public non-sealed interface R_Wildcard

      extends C_Wildcard,
              R_Type
{
   /**
    * {@snippet :
    *  List<? extends Number>//@highlight substring="? extends Number"
    *}
    */
   Optional<R_Type> getExtends();

   /**
    * {@snippet :
    *  List<? super Number>//@highlight substring="? super Number"
    *}
    */
   Optional<R_Type> getSuper();

   /**
    * {@snippet file = "WildcardTest.java" region = "Wildcard.contains"}
    */
   boolean contains(C_Type type);
}
