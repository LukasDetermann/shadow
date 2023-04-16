package io.determann.shadow.api.shadow;

public interface EnumConstant extends Variable<Enum>
{
   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
