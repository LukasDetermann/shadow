package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.dsl.TypeRenderable;
import io.determann.shadow.api.dsl.result.ResultRenderable;

public interface MethodResultStep
{
   MethodNameStep result(String result);

   MethodNameStep result(ResultRenderable result);

   MethodNameStep resultType(String resultType);

   MethodNameStep resultType(TypeRenderable resultType);
}
