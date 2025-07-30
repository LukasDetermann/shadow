package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.dsl.receiver.ReceiverRenderable;

public interface ConstructorReceiverStep extends ConstructorParameterStep
{
   ConstructorParameterStep receiver(String receiver);

   ConstructorParameterStep receiver(ReceiverRenderable receiver);
}
