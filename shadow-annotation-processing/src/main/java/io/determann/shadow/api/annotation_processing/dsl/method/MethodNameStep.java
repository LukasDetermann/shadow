package io.determann.shadow.api.annotation_processing.dsl.method;

import org.jetbrains.annotations.Contract;

public interface MethodNameStep
{
   @Contract(value = "_ -> new", pure = true)
   MethodReceiverStep name(String name);
}
