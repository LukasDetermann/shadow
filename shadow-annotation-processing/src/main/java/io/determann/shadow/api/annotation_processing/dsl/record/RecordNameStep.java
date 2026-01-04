package io.determann.shadow.api.annotation_processing.dsl.record;

import org.jetbrains.annotations.Contract;

public interface RecordNameStep
{
   @Contract(value = "_ -> new", pure = true)
   RecordRecordComponentStep name(String name);
}
