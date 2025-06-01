package io.determann.shadow.api.dsl.field;

import io.determann.shadow.api.shadow.type.C_FieldType;

public interface FieldTypeStep
{
   FieldNameStep type(String type);

   FieldNameStep type(C_FieldType type);

   FieldNameStep type(FieldTypeRenderable type);
}
