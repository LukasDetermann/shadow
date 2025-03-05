package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.shadow.type.C_Class;

public interface ConstructorThrowsStep extends ConstructorBodyStep
{
   ConstructorThrowsStep throws_(String... exception);

   ConstructorThrowsStep throws_(C_Class... exception);
}
