package io.determann.shadow.api.annotation_processing.dsl.result;

import io.determann.shadow.api.annotation_processing.dsl.TypeRenderable;
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