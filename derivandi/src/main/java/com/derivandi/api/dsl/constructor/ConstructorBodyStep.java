package com.derivandi.api.dsl.constructor;

import org.jetbrains.annotations.Contract;

public interface ConstructorBodyStep
      extends ConstructorRenderable
{
   @Contract(value = "_ -> new", pure = true)
   ConstructorRenderable body(String body);
}
