package com.derivandi.api.dsl.result;

import com.derivandi.api.dsl.TypeRenderable;
import org.jetbrains.annotations.Contract;

public interface ResultTypeStep
{
   @Contract(value = "_ -> new", pure = true)
   ResultRenderable type(String type);

   @Contract(value = "_ -> new", pure = true)
   ResultRenderable type(TypeRenderable type);

   @Contract(value = "-> new", pure = true)
   ResultRenderable surroundingType();
}