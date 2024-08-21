package io.determann.shadow.api;


public sealed interface Operation<TYPE, RESULT> permits AbstractOperation
{
   String getName();
}
