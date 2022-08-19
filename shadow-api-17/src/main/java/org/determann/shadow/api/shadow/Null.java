package org.determann.shadow.api.shadow;

import javax.lang.model.type.NullType;

public interface Null extends Shadow<NullType>
{
   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
