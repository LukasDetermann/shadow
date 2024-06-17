package io.determann.shadow.meta_meta;

public sealed interface Operation<TYPE, RESULT> permits AbstractOperation
{
   String getName();
}
