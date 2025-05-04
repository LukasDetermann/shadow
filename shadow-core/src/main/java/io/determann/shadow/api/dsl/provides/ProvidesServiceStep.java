package io.determann.shadow.api.dsl.provides;

import io.determann.shadow.api.shadow.type.C_Declared;

public interface ProvidesServiceStep
{
   ProvidesImplementationStep service(String serviceName);

   ProvidesImplementationStep service(C_Declared service);
}
