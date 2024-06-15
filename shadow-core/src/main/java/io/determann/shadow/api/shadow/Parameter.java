package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.FinalModifiable;

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
