package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.StaticModifiable;

import java.util.List;

import static io.determann.shadow.meta_meta.Operations.NAMEABLE_NAME;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

public interface Enum extends Declared,
                              StaticModifiable
{
   List<EnumConstant> getEumConstants();

   default EnumConstant getEnumConstantOrThrow(String simpleName)
   {
      return getEumConstants().stream().filter(field -> requestOrThrow(field, NAMEABLE_NAME).equals(simpleName)).findAny().orElseThrow();
   }
}
