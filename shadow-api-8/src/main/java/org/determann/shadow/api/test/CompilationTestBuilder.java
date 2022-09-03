package org.determann.shadow.api.test;

import org.determann.shadow.impl.test.CompilationTestBuilderImpl;

import java.io.File;

/**
 * Builder to test annotation processors
 */
public interface CompilationTestBuilder
{
   public static CompilationTestBuilder process(ProcessingCallback processingCallback)
   {
      return new CompilationTestBuilderImpl(processingCallback);
   }

   /**
    * adds an existing file to compile
    */
   CompilationTestBuilder withFileToCompile(File fileToCompile);

   /**
    * adds a new file to compile
    *
    * @param fileName MyClass.java
    * @param sourceCode the sourceCode of the file
    */
   CompilationTestBuilder withFileToCompile(String fileName, String sourceCode);

   /**
    * This already compiled class will also be processed
    *
    * @param withCompiledClassName java.lang.Object
    */
   CompilationTestBuilder withCompiledClassName(String withCompiledClassName);

   /**
    * This already compiled class will also be processed
    */
   CompilationTestBuilder withCompiledClassName(Class<?> withCompiledClass);

   /**
    * Compiler options
    */
   CompilationTestBuilder withOption(String option);

   /**
    * Compiles the classes and invokes the {@link ProcessingCallback}
    */
   void compile();
}
