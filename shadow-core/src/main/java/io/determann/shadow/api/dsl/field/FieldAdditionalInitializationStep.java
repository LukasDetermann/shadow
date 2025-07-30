package io.determann.shadow.api.dsl.field;

public interface FieldAdditionalInitializationStep extends FieldRenderable
{
   FieldAdditionalNameStep initializer(String initializer);
}
