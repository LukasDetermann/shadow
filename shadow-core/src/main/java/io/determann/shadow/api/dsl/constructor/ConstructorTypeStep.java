package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.dsl.class_.ClassRenderable;
import io.determann.shadow.api.dsl.enum_.EnumRenderable;
import io.determann.shadow.api.dsl.record.RecordRenderable;

public interface ConstructorTypeStep
{
   ConstructorReceiverStep type(String type);

   ConstructorReceiverStep type(ClassRenderable type);

   ConstructorReceiverStep type(EnumRenderable type);

   ConstructorReceiverStep type(RecordRenderable type);
}
