package io.determann.shadow.api.dsl.record_component;

import io.determann.shadow.api.dsl.TypeRenderable;

public interface RecordComponentTypeStep
{
   RecordComponentRenderable type(String type);

   RecordComponentRenderable type(TypeRenderable type);
}
