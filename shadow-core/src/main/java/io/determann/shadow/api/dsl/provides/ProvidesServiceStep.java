package io.determann.shadow.api.dsl.provides;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import org.jetbrains.annotations.Contract;

public interface ProvidesServiceStep
{
   @Contract(value = "_ -> new", pure = true)
   ProvidesImplementationStep service(String serviceName);

   @Contract(value = "_ -> new", pure = true)
   ProvidesImplementationStep service(DeclaredRenderable service);
}
