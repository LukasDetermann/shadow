package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.dsl.receiver.ReceiverRenderable;

public interface MethodReceiverStep extends MethodParameterStep
{
   MethodParameterStep receiver(String receiver);

   MethodParameterStep receiver(ReceiverRenderable receiver);
}
