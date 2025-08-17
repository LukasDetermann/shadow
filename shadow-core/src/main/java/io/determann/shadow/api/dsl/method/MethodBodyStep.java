package io.determann.shadow.api.dsl.method;

import org.jetbrains.annotations.Contract;

public interface MethodBodyStep
      extends MethodRenderable
{
   @Contract(value = "_ -> new", pure = true)
   MethodRenderable body(String body);
}
