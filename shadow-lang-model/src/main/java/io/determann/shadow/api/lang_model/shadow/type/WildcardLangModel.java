package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.api.shadow.type.Wildcard;

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
public interface WildcardLangModel extends Wildcard,
                                           ShadowLangModel
{
   /**
    * {@snippet :
    *  List<? extends Number>//@highlight substring="? extends Number"
    *}
    */
   Optional<ShadowLangModel> getExtends();

   /**
    * {@snippet :
    *  List<? super Number>//@highlight substring="? super Number"
    *}
    */
   Optional<ShadowLangModel> getSuper();

   /**
    * {@snippet file = "WildcardTest.java" region = "Wildcard.contains"}
    */
   boolean contains(Shadow shadow);
}
