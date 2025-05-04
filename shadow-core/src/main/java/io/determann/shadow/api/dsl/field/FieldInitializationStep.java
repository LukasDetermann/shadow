package io.determann.shadow.api.dsl.field;

public interface FieldInitializationStep extends FieldRenderable
{
   FieldAdditionalNameStep initializer(String initializer);
}
