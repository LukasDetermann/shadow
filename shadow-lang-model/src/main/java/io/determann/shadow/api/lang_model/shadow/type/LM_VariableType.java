package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.shadow.type.primitive.LM_Primitive;
import io.determann.shadow.api.shadow.type.C_VariableType;

/// a type that can be used as method/ constructor parameter or field
public sealed interface LM_VariableType
      extends C_VariableType,
              LM_Type
      permits LM_ReferenceType,
              LM_Primitive {}