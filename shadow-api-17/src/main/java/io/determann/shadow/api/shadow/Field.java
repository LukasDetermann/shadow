package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.AccessModifiable;
import io.determann.shadow.api.modifier.FinalModifiable;
import io.determann.shadow.api.modifier.StaticModifiable;

import javax.lang.model.type.TypeMirror;

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
    * convince method returns the erasure of the field type
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
