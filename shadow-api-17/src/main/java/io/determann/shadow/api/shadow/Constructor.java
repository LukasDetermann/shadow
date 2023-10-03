package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.AccessModifiable;

public interface Constructor extends Executable,
                                     AccessModifiable
{
   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
