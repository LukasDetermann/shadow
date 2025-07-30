package io.determann.shadow.api.query;

import io.determann.shadow.api.query.operation.Operation;

public interface ProviderSpi
{
   Implementation getImplementation();

   <RESULT> Response<RESULT> request(Object instance, Operation<RESULT> operation, Object... params);
}
