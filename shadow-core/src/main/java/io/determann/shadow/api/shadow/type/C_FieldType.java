package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

/// All the Types a Field can have
public sealed interface C_FieldType
      permits C_Array,
              C_Declared,
              C_Generic,
              C_Primitive
{
}
