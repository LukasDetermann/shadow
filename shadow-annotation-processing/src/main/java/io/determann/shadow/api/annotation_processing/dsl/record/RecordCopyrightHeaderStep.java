package io.determann.shadow.api.annotation_processing.dsl.record;


import org.jetbrains.annotations.Contract;

public interface RecordCopyrightHeaderStep
      extends RecordPackageStep
{
   @Contract(value = "_ -> new", pure = true)
   RecordPackageStep copyright(String copyrightHeader);
}
