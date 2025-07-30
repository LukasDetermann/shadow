package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.type.primitive.R_Primitive;
import io.determann.shadow.api.shadow.type.C_VariableType;

/// a type that can be used as method/ constructor parameter or field
public sealed interface R_VariableType
      extends C_VariableType,
              R_Type
      permits R_ReferenceType,
              R_Primitive {}
