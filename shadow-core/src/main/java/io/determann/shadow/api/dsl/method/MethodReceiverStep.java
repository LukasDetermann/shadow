package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.dsl.receiver.ReceiverRenderable;
import org.jetbrains.annotations.Contract;

public interface MethodReceiverStep
      extends MethodParameterStep
{
   @Contract(value = "_ -> new", pure = true)
   MethodParameterStep receiver(String receiver);

   @Contract(value = "_ -> new", pure = true)
   MethodParameterStep receiver(ReceiverRenderable receiver);
}
