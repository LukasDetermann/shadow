package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.shadow.C_Erasable;

/**
 * {@snippet id = "test":
 *  List<? extends Number>//@highlight substring="? extends Number"
 *}
 * or
 * {@snippet :
 *  List<? super Number>//@highlight substring="? super Number"
 *}
 */
public interface C_Wildcard extends C_Type,
                                    C_Erasable
{
}
