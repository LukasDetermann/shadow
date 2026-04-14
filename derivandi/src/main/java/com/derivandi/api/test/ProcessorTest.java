package com.derivandi.api.test;

import com.derivandi.api.processor.ProcessingCallback;
import com.derivandi.api.processor.SimpleContext;
import com.derivandi.internal.test.ProcessorTestImpl;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.processing.Processor;
import java.io.File;
import java.nio.file.Path;

/**
 * Runs annotation processors. Generated files will not be written to disk.
 * May get moved it a separate artifact in the future
 */
@ApiStatus.Experimental
public interface ProcessorTest
{
   static ProcessorTest processorTest()
   {
      return new ProcessorTestImpl();
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

   void process(ProcessingCallback<SimpleContext> processingCallback);

   void process(Processor... processors);
}
