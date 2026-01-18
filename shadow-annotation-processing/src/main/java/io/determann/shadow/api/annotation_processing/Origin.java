package io.determann.shadow.api.annotation_processing;

public enum Origin
{
   SOURCE_DECLARED,
   LANGUAGE_REQUIRED,
   COMPILER_GENERATED;

   public boolean isDeclared() {
      return this != COMPILER_GENERATED;
   }
}
