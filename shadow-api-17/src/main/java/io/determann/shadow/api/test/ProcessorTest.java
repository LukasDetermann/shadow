package io.determann.shadow.api.test;

import io.determann.shadow.impl.test.ProcessorTestImpl;

import java.io.File;

/**
 * Builder to test annotation processors
 */
public interface ProcessorTest
{
   public static ProcessorTest process(ProcessingCallback processingCallback)
   {
      return new ProcessorTestImpl(processingCallback);
   }

   /**
    * adds an existing file to compile
    *
    * @return a new {@link ProcessorTest}
    */
   ProcessorTest withCodeToCompile(File fileToCompile);

   /**
    * adds a new file to compile
    *
    * @param fileName MyClass.java
    * @param code the sourceCode of the file
    *
    * @return a new {@link ProcessorTest}
    */
   ProcessorTest withCodeToCompile(String fileName, String code);

   /**
    * This already compiled class will also be processed
    *
    * @param qualifiedName java.lang.Object
    *
    * @return a new {@link ProcessorTest}
    */
   ProcessorTest withCompiledClass(String qualifiedName);

   /**
    * This already compiled class will also be processed
    *
    * @return a new {@link ProcessorTest}
    */
   ProcessorTest withCompiledClass(Class<?> aClass);

   /**
    * Compiler options
    *
    * @return a new {@link ProcessorTest}
    */
   ProcessorTest withOption(String option);

   /**
    * Compiles the classes and invokes the {@link ProcessingCallback}
    */
   void compile();
}
