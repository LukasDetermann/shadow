package io.determann.shadow.api.query.operation;

public interface TriFunction<PARAM_1, PARAM_2, PARAM_3, RESULT>
{
   RESULT apply(PARAM_1 param1, PARAM_2 param2, PARAM_3 param3);
}
