package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.shadow.modifier.FinalModifiable;

/**
 * Parameter of a method or constructor
 *
 * @see Method#getParameters()
 * @see Constructor#getParameters()
 */
public interface Parameter extends Variable,
                                   FinalModifiable
{
}
