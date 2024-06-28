package io.determann.shadow.api.shadow;


public sealed interface Operation<TYPE, RESULT> permits AbstractOperation
{
   String getName();
}
