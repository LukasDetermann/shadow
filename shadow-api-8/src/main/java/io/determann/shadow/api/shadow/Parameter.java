package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.FinalModifiable;

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
    * {@link java.util.Arrays#asList(Object[])}
    */
   boolean isVarArgs();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
