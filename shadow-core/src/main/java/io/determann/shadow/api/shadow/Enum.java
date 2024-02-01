package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.StaticModifiable;

import java.util.List;

public interface Enum extends Declared,
                              StaticModifiable
{
   List<EnumConstant> getEumConstants();

   default EnumConstant getEnumConstantOrThrow(String simpleName)
   {
      return getEumConstants().stream().filter(field -> field.getName().equals(simpleName)).findAny().orElseThrow();
   }
}
