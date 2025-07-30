package io.determann.shadow.api.dsl.field;

import io.determann.shadow.api.dsl.VariableTypeRenderable;

public interface FieldTypeStep
{
   FieldNameStep type(String type);

   FieldNameStep type(VariableTypeRenderable type);
}
