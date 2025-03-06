package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.shadow.structure.C_Receiver;

public interface MethodReceiverStep extends MethodParameterStep
{
   MethodParameterStep receiver(String receiver);

   MethodParameterStep receiver(C_Receiver receiver);
}
