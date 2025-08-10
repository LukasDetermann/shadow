package io.determann.shadow.api.dsl.record_component;

import io.determann.shadow.api.dsl.TypeRenderable;

public interface RecordComponentTypeStep
{
   RecordComponentNameStep type(String type);

   RecordComponentNameStep type(TypeRenderable type);
}
