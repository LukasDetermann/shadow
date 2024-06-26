package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.shadow.type.Intersection;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.List;

/**
 * {@snippet :
 * T extends Collection & Serializable//@highlight substring="Collection & Serializable"
 *}
 */
public interface IntersectionLangModel extends ShadowLangModel,
                                               Intersection
{
   /**
    * {@snippet :
    * T extends Collection & Serializable//@highlight substring="Collection & Serializable"
    *}
    */
   List<Shadow> getBounds();
}
