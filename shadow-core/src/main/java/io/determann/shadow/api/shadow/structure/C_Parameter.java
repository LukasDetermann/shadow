package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.shadow.modifier.C_FinalModifiable;

/**
 * Parameter of a method or constructor
 *
 * @see C_Method#getParameters()
 * @see C_Constructor#getParameters()
 */
public interface C_Parameter extends C_Variable,
                                     C_FinalModifiable
{
}
