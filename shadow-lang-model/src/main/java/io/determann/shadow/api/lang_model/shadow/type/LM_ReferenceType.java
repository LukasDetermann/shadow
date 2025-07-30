package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.shadow.type.C_ReferenceType;

public sealed interface LM_ReferenceType extends C_ReferenceType,
                                                 LM_VariableType
      permits LM_Array,
              LM_Declared,
              LM_Generic {}