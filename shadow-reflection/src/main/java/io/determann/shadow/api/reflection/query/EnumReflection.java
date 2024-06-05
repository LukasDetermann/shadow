package io.determann.shadow.api.reflection.query;

import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.EnumConstant;

import java.util.List;

import static io.determann.shadow.meta_meta.Operations.NAMEABLE_NAME;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

public interface EnumReflection extends Enum,
                                        DeclaredReflection
{
   List<EnumConstant> getEumConstants();

   default EnumConstant getEnumConstantOrThrow(String simpleName)
   {
      return getEumConstants().stream().filter(field -> requestOrThrow(field, NAMEABLE_NAME).equals(simpleName)).findAny().orElseThrow();
   }
}
