package com.derivandi.api.dsl.record;

import org.jetbrains.annotations.Contract;

public interface RecordNameStep
{
   @Contract(value = "_ -> new", pure = true)
   RecordRecordComponentStep name(String name);
}
