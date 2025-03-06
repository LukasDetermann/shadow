package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.shadow.type.C_Class;

public interface MethodThrowsStep extends MethodBodyStep
{
   MethodThrowsStep throws_(String... exception);

   MethodThrowsStep throws_(C_Class... exception);
}
