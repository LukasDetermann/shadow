package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.modifier.StaticModifiableReflection;
import io.determann.shadow.api.reflection.shadow.structure.EnumConstantReflection;
import io.determann.shadow.api.shadow.type.Enum;

import java.util.List;

import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;

public interface EnumReflection extends Enum,
                                        DeclaredReflection,
                                        StaticModifiableReflection
{
   List<EnumConstantReflection> getEumConstants();

   default EnumConstantReflection getEnumConstantOrThrow(String simpleName)
   {
      return getEumConstants().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).findAny().orElseThrow();
   }
}
