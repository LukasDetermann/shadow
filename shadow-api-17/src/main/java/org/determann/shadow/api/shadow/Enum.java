package org.determann.shadow.api.shadow;

import org.determann.shadow.api.modifier.StaticModifiable;

import java.util.List;

public interface Enum extends Declared,
                              StaticModifiable
{
   List<Interface> getDirectInterfaces();

   List<EnumConstant> getEumConstants();

   EnumConstant getEnumConstant(String simpleName);

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
