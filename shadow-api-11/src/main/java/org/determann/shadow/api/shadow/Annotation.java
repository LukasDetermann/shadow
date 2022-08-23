package org.determann.shadow.api.shadow;

import org.determann.shadow.api.modifier.StaticModifiable;

public interface Annotation extends Declared,
                                    StaticModifiable
{
   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
