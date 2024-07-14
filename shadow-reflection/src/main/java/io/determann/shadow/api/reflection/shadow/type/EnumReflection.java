package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.type.Enum;

import java.util.List;

import static io.determann.shadow.api.shadow.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public interface EnumReflection extends Enum,
                                        DeclaredReflection
{
   List<EnumConstant> getEumConstants();

   default EnumConstant getEnumConstantOrThrow(String simpleName)
   {
      return getEumConstants().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).findAny().orElseThrow();
   }
}
