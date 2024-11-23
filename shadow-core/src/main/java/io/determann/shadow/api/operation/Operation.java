package io.determann.shadow.api.operation;

public sealed interface Operation<RESULT> permits AbstractOperation,
                                                  InstanceOperation,
                                                  StaticOperation
{
   String getName();
}
