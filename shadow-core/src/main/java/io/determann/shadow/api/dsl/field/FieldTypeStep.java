package io.determann.shadow.api.dsl.field;

public interface FieldTypeStep
{
   FieldNameStep type(String type);

   FieldNameStep type(FieldType type);

   FieldNameStep type(FieldTypeRenderable type);
}
