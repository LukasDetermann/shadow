package com.derivandi.api.dsl.record_component;

import com.derivandi.api.dsl.TypeRenderable;
import org.jetbrains.annotations.Contract;

public interface RecordComponentTypeStep
{
   @Contract(value = "_ -> new", pure = true)
   RecordComponentNameStep type(String type);

   @Contract(value = "_ -> new", pure = true)
   RecordComponentNameStep type(TypeRenderable type);
}
