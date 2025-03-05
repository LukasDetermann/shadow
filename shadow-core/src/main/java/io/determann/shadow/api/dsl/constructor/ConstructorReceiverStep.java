package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.shadow.structure.C_Receiver;

public interface ConstructorReceiverStep extends ConstructorParameterStep
{
   ConstructorParameterStep receiver(String receiver);

   ConstructorParameterStep receiver(C_Receiver receiver);
}
