package com.derivandi.api;

public enum Origin
{
   SOURCE_DECLARED,
   LANGUAGE_REQUIRED,
   COMPILER_GENERATED;

   public boolean isDeclared() {
      return this != COMPILER_GENERATED;
   }
}
