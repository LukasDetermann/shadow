package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.dsl.result.ResultRenderable;
import io.determann.shadow.api.shadow.structure.C_Result;
import io.determann.shadow.api.shadow.type.C_Type;

public interface MethodResultStep
{
   MethodNameStep result(String result);

   MethodNameStep result(C_Result result);

   MethodNameStep result(ResultRenderable result);

   MethodNameStep resultType(String resultType);

   MethodNameStep resultType(C_Type resultType);
}
