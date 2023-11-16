package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.FinalModifiable;

import java.util.List;

/**
 * Parameter of a method or constructor
 *
 * @see Method#getParameters()
 * @see Constructor#getParameters()
 */
public interface Parameter extends Variable<Executable>,
                                   FinalModifiable
{
   /**
    * {@link List#of(Object[])}
    */
   boolean isVarArgs();
}
