package org.determann.shadow.api.shadow;

import org.determann.shadow.api.modifier.StaticModifiable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;

public interface Enum extends Declared,
                              StaticModifiable
{
   @UnmodifiableView List<EnumConstant> getEumConstants();

   EnumConstant getEnumConstantOrThrow(String simpleName);

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
