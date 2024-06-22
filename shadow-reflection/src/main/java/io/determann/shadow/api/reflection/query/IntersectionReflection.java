package io.determann.shadow.api.reflection.query;

import io.determann.shadow.api.shadow.Intersection;
import io.determann.shadow.api.shadow.Shadow;

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
   List<Shadow> getBounds();
}
