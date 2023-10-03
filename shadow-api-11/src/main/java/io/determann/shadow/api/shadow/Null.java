package io.determann.shadow.api.shadow;

public interface Null extends Shadow
{
   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
