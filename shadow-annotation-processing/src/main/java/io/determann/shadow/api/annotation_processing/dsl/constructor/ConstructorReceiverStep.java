package io.determann.shadow.api.annotation_processing.dsl.constructor;

import io.determann.shadow.api.annotation_processing.dsl.receiver.ReceiverRenderable;
import org.jetbrains.annotations.Contract;

public interface ConstructorReceiverStep
      extends ConstructorParameterStep
{
   @Contract(value = "_ -> new", pure = true)
   ConstructorParameterStep receiver(String receiver);

   @Contract(value = "_ -> new", pure = true)
   ConstructorParameterStep receiver(ReceiverRenderable receiver);
}
