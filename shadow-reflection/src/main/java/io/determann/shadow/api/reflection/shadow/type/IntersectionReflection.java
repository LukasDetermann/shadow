package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.shadow.type.Intersection;

import java.util.List;

/**
 * {@snippet :
 * T extends Collection & Serializable//@highlight substring="Collection & Serializable"
 *}
 */
public interface IntersectionReflection extends ShadowReflection,
                                                Intersection
{
   /**
    * {@snippet :
    * T extends Collection & Serializable//@highlight substring="Collection & Serializable"
    *}
    */
   List<ShadowReflection> getBounds();
}
