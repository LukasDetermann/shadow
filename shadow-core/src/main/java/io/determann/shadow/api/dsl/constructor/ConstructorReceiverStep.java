package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.dsl.receiver.ReceiverRenderable;
import org.jetbrains.annotations.Contract;

public interface ConstructorReceiverStep
      extends ConstructorParameterStep
{
   @Contract(value = "_ -> new", pure = true)
   ConstructorParameterStep receiver(String receiver);

   @Contract(value = "_ -> new", pure = true)
   ConstructorParameterStep receiver(ReceiverRenderable receiver);
}
