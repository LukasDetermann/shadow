package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.AccessModifiable;
import io.determann.shadow.api.modifier.FinalModifiable;
import io.determann.shadow.api.modifier.StaticModifiable;

public interface Field extends Variable<Declared>,
                               AccessModifiable,
                               FinalModifiable,
                               StaticModifiable
{
   boolean isConstant();

   /**
    * String or primitive value of static fields
    */
   Object getConstantValue();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
