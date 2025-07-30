package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.shadow.type.C_ReferenceType;

public sealed interface R_ReferenceType
      extends R_VariableType,
              C_ReferenceType
      permits R_Array,
              R_Declared,
              R_Generic {}
