package io.determann.shadow.meta_meta;

import io.determann.shadow.api.ImplementationDefined;

public interface ProviderSpi
{
   public String getImplementationName();

   <RESULT, TYPE extends ImplementationDefined> Response<RESULT> request(TYPE instance, Operation<TYPE, RESULT> operation, Object... params);
}
