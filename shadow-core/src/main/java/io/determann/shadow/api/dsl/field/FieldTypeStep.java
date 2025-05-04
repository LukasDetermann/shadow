package io.determann.shadow.api.dsl.field;

import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

public interface FieldTypeStep
{
   FieldNameStep type(String type);

   FieldNameStep type(C_Primitive primitive);

   FieldNameStep type(C_Array array);

   FieldNameStep type(C_Declared declared);
}
