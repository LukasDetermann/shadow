package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.shadow.C_Erasable;

/**
 * {@snippet :
 * T extends Collection & Serializable//@highlight substring="Collection & Serializable"
 *}
 */
public interface C_Intersection extends C_Type,
                                        C_Erasable
{
}
