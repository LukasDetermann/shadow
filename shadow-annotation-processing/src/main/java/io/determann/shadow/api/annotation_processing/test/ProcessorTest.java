package io.determann.shadow.api.annotation_processing.test;

import io.determann.shadow.internal.test.ProcessorTestImpl;

import javax.annotation.processing.Processor;
import java.io.File;
import java.nio.file.Path;

/**
 * Builder to test annotation processors
 */
public interface ProcessorTest
{
   public static ProcessorTest process(ProcessingCallback processingCallback)
   {
      return new ProcessorTestImpl(processingCallback);
   }

   @SafeVarargs
   public static ProcessorTest process(Class<? extends Processor>... processingCallback)
   {
      return new ProcessorTestImpl(processingCallback);
   }

   /**
    * adds an existing file to compile
    *
    * @return a new {@link ProcessorTest}
    */
   ProcessorTest withCodeToCompile(Path fileToCompile);

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
