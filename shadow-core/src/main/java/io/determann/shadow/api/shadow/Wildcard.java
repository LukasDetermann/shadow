package io.determann.shadow.api.shadow;

import java.util.Optional;

/**
 * {@snippet id="test":
 *  List<? extends Number>//@highlight substring="? extends Number"
 * }
 * or
 * {@snippet :
 *  List<? super Number>//@highlight substring="? super Number"
 * }
 */
public interface Wildcard extends Shadow
{
   /**
    * {@snippet :
    *  List<? extends Number>//@highlight substring="? extends Number"
    * }
    */
   Optional<Shadow> getExtends();

   /**
    * {@snippet :
    *  List<? super Number>//@highlight substring="? super Number"
    * }
    */
   Optional<Shadow> getSuper();

   /**
    * {@snippet file= "WildcardTest.java" region="Wildcard.contains"}
    */
   boolean contains(Shadow shadow);
}
