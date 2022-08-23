package org.determann.shadow.api.shadow;

import javax.lang.model.type.NoType;

public interface Void extends Shadow<NoType>
{
   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
