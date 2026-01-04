package io.determann.shadow.api.annotation_processing.dsl.method;

import io.determann.shadow.api.annotation_processing.dsl.TypeRenderable;
import io.determann.shadow.api.annotation_processing.dsl.result.ResultRenderable;
import org.jetbrains.annotations.Contract;

public interface MethodResultStep
{
   @Contract(value = "_ -> new", pure = true)
   MethodNameStep result(String result);

   @Contract(value = "_ -> new", pure = true)
   MethodNameStep result(ResultRenderable result);

   @Contract(value = "_ -> new", pure = true)
   MethodNameStep resultType(String resultType);

   @Contract(value = "_ -> new", pure = true)
   MethodNameStep resultType(TypeRenderable resultType);

   @Contract(value = "-> new", pure = true)
   MethodNameStep surroundingResultType();
}