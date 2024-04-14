package io.determann.shadow.api.shadow;

import java.util.List;

/**
 * {@snippet :
 * T extends Collection & Serializable//@highlight substring="Collection & Serializable"
 *}
 */
public interface Intersection extends Shadow
{
   /**
    * {@snippet :
    * T extends Collection & Serializable//@highlight substring="Collection & Serializable"
    *}
    */
   List<Shadow> getBounds();
}
