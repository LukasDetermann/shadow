package org.determann.shadow.api.shadow;

import org.determann.shadow.api.modifier.AccessModifiable;
import org.determann.shadow.api.modifier.FinalModifiable;
import org.determann.shadow.api.modifier.StaticModifiable;

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
