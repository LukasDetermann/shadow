package io.determann.shadow.api.annotation_processing.dsl.provides;

import io.determann.shadow.api.annotation_processing.dsl.declared.DeclaredRenderable;
import org.jetbrains.annotations.Contract;

public interface ProvidesServiceStep
{
   @Contract(value = "_ -> new", pure = true)
   ProvidesImplementationStep service(String serviceName);

   @Contract(value = "_ -> new", pure = true)
   ProvidesImplementationStep service(DeclaredRenderable service);
}
