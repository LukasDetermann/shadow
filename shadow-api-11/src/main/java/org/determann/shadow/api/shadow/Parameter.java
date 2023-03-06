package org.determann.shadow.api.shadow;

import org.determann.shadow.api.modifier.FinalModifiable;

import javax.lang.model.type.TypeMirror;

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
   Shadow<TypeMirror> erasure();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
