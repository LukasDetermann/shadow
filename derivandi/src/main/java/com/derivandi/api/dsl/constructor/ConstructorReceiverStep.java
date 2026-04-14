package com.derivandi.api.dsl.constructor;

import com.derivandi.api.dsl.receiver.ReceiverRenderable;
import org.jetbrains.annotations.Contract;

public interface ConstructorReceiverStep
      extends ConstructorParameterStep
{
   @Contract(value = "_ -> new", pure = true)
   ConstructorParameterStep receiver(String receiver);

   @Contract(value = "_ -> new", pure = true)
   ConstructorParameterStep receiver(ReceiverRenderable receiver);
}
