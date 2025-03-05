package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Enum;
import io.determann.shadow.api.shadow.type.C_Record;

public interface ConstructorTypeStep
{
   ConstructorReceiverStep constructor(String type);

   ConstructorReceiverStep constructor(C_Class type);

   ConstructorReceiverStep constructor(C_Enum type);

   ConstructorReceiverStep constructor(C_Record type);
}
