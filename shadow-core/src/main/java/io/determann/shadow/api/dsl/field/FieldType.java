package io.determann.shadow.api.dsl.field;

import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

/// All the Types a Field can have
public sealed interface FieldType
      permits C_Array,
              C_Declared,
              C_Generic,
              C_Primitive
{
}
