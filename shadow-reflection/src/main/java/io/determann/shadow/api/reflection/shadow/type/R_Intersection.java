package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.shadow.type.C_Intersection;

import java.util.List;

/**
 * {@snippet :
 * T extends Collection & Serializable//@highlight substring="Collection & Serializable"
 *}
 */
public non-sealed interface R_Intersection

      extends R_Type,
              C_Intersection
{
   /**
    * {@snippet :
    * T extends Collection & Serializable//@highlight substring="Collection & Serializable"
    *}
    */
   List<R_Type> getBounds();
}
