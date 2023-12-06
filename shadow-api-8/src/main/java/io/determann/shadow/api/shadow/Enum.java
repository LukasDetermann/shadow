package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.StaticModifiable;

import java.util.List;
import java.util.NoSuchElementException;

public interface Enum extends Declared,
                              StaticModifiable
{
   List<EnumConstant> getEumConstants();

   default EnumConstant getEnumConstantOrThrow(String simpleName)
   {
      return getEumConstants().stream()
                              .filter(field -> field.getName().equals(simpleName))
                              .findAny()
                              .orElseThrow(NoSuchElementException::new);
   }
}
