package com.derivandi.api.dsl.method;

import com.derivandi.api.dsl.TypeRenderable;
import com.derivandi.api.dsl.result.ResultRenderable;
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