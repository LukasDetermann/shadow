package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.dsl.class_.ClassRenderable;
import io.determann.shadow.api.dsl.enum_.EnumRenderable;
import io.determann.shadow.api.dsl.record.RecordRenderable;
import org.jetbrains.annotations.Contract;

public interface ConstructorTypeStep
{
   @Contract(value = "_ -> new", pure = true)
   ConstructorReceiverStep type(String type);

   @Contract(value = "_ -> new", pure = true)
   ConstructorReceiverStep type(ClassRenderable type);

   @Contract(value = "_ -> new", pure = true)
   ConstructorReceiverStep type(EnumRenderable type);

   @Contract(value = "_ -> new", pure = true)
   ConstructorReceiverStep type(RecordRenderable type);

   @Contract(value = "-> new", pure = true)
   ConstructorReceiverStep surroundingType();
}