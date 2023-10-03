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
    * convince method returns the erasure of the parameter type
    *
    * @see Class#erasure() for example for more information on erasure
    */
   Shadow erasure();

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
