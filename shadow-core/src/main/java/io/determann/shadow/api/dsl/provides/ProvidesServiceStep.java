package io.determann.shadow.api.dsl.provides;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;

public interface ProvidesServiceStep
{
   ProvidesImplementationStep service(String serviceName);

   ProvidesImplementationStep service(DeclaredRenderable service);
}
