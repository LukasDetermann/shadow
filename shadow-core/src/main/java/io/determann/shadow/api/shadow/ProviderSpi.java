package io.determann.shadow.api.shadow;

import io.determann.shadow.api.ImplementationDefined;

public interface ProviderSpi
{
   public String getImplementationName();

   <RESULT, TYPE extends ImplementationDefined> Response<RESULT> request(TYPE instance, Operation<? super TYPE, RESULT> operation, Object... params);
}
