package io.determann.shadow.api;

import io.determann.shadow.api.operation.Operation;

public interface ProviderSpi
{
   public Implementation getImplementation();

   <RESULT> Response<RESULT> request(Object instance, Operation<RESULT> operation, Object... params);
}
