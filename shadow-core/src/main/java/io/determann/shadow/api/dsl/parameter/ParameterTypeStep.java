package io.determann.shadow.api.dsl.parameter;

import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

public interface ParameterTypeStep
{
   ParameterNameStep type(String type);

   ParameterNameStep type(C_Array array);

   ParameterNameStep type(C_Primitive primitive);

   ParameterNameStep type(C_Declared declared);

   ParameterNameStep type(C_Generic generic);
}
