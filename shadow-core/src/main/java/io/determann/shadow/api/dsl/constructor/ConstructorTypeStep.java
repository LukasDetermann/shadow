package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.dsl.class_.ClassRenderable;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Enum;
import io.determann.shadow.api.shadow.type.C_Record;

public interface ConstructorTypeStep
{
   ConstructorReceiverStep type(String type);

   ConstructorReceiverStep type(C_Class type);

   ConstructorReceiverStep type(ClassRenderable type);

   ConstructorReceiverStep type(C_Enum type);

   ConstructorReceiverStep type(C_Record type);
}
