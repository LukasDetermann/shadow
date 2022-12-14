package org.determann.shadow.api.test;

import org.determann.shadow.api.metadata.QualifiedName;
import org.determann.shadow.impl.test.CompilationTestImpl;
import org.intellij.lang.annotations.Language;

import java.io.File;

/**
 * Builder to test annotation processors
 */
public interface CompilationTest
{
   public static CompilationTest process(ProcessingCallback processingCallback)
   {
      return new CompilationTestImpl(processingCallback);
   }

   /**
    * adds an existing file to compile
    *
    * @return a new {@link CompilationTest}
    */
   CompilationTest withCodeToCompile(File fileToCompile);

   /**
    * adds a new file to compile
    *
    * @param fileName MyClass.java
    * @param code the sourceCode of the file
    *
    * @return a new {@link CompilationTest}
    */
   CompilationTest withCodeToCompile(String fileName, @Language("JAVA") String code);

   /**
    * This already compiled class will also be processed
    *
    * @param qualifiedName java.lang.Object
    *
    * @return a new {@link CompilationTest}
    */
   CompilationTest withCompiledClass(@QualifiedName String qualifiedName);

   /**
    * This already compiled class will also be processed
    *
    * @return a new {@link CompilationTest}
    */
   CompilationTest withCompiledClass(Class<?> aClass);

   /**
    * Compiler options
    *
    * @return a new {@link CompilationTest}
    */
   CompilationTest withOption(String option);

   /**
    * Compiles the classes and invokes the {@link ProcessingCallback}
    */
   void compile();
}
