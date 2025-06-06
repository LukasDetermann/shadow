package io.determann.shadow.api.dsl.field;

import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

public interface FieldTypeStep
{
   FieldNameStep type(String type);

   FieldNameStep type(C_Array type);

   FieldNameStep type(C_Declared type);

   FieldNameStep type(C_Generic type);

   FieldNameStep type(C_Primitive type);

   FieldNameStep type(FieldTypeRenderable type);
}
