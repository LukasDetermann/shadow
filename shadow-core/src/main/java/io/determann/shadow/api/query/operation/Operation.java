package io.determann.shadow.api.query.operation;

public sealed interface Operation<RESULT> permits AbstractOperation,
                                                  InstanceOperation,
                                                  StaticOperation
{
   String getName();
}
