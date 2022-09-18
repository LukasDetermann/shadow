package org.determann.shadow.impl.test;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.ShadowProcessor;
import org.determann.shadow.api.test.CompilationTest;
import org.determann.shadow.api.test.ProcessingCallback;

import javax.annotation.processing.Processor;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CompilationTestImpl implements CompilationTest
{
   private final ProcessingCallback processingCallback;
   private final List<JavaFileObject> toCompile;
   private final List<String> options;
   private final List<String> compiledClassNames;

   private CompilationTestImpl(ProcessingCallback processingCallback,
                               List<JavaFileObject> toCompile,
                               List<String> options,
                               List<String> compiledClassNames)
   {
      this.processingCallback = processingCallback;
      this.toCompile = toCompile;
      this.options = options;
      this.compiledClassNames = compiledClassNames;
   }

   public CompilationTestImpl(ProcessingCallback processingCallback)
   {
      this.processingCallback = processingCallback;
      toCompile = new ArrayList<>();
      options = new ArrayList<>();
      compiledClassNames = new ArrayList<>();
   }

   @Override
   public CompilationTest withCodeToCompile(File fileToCompile)
   {
      if (!fileToCompile.getName().endsWith(".java"))
      {
         throw new IllegalArgumentException("can only compile .java files");
      }

      String sourceCode;
      try
      {
         sourceCode = String.join("\n", Files.readAllLines(fileToCompile.toPath()));
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }

      toCompile.add(createJavaFileObject(fileToCompile.toURI(), sourceCode));
      return new CompilationTestImpl(processingCallback, toCompile, options, compiledClassNames);
   }

   @Override
   public CompilationTest withCodeToCompile(String fileName, String code)
   {
      toCompile.add(createJavaFileObject(createUri(fileName), code));
      return new CompilationTestImpl(processingCallback, toCompile, options, compiledClassNames);
   }

   @Override
   public CompilationTest withOption(String option)
   {
      this.options.add(option);
      return new CompilationTestImpl(processingCallback, toCompile, options, compiledClassNames);
   }

   @Override
   public CompilationTest withCompiledClass(String qualifiedName)
   {
      this.compiledClassNames.add(qualifiedName);
      return new CompilationTestImpl(processingCallback, toCompile, options, compiledClassNames);
   }

   @Override
   public CompilationTest withCompiledClass(Class<?> aClass)
   {
      this.compiledClassNames.add(aClass.getName());
      return new CompilationTestImpl(processingCallback, toCompile, options, compiledClassNames);
   }

   @Override
   public void compile()
   {
      if (toCompile.isEmpty() && compiledClassNames.isEmpty())
      {
         throw new IllegalStateException("Noting to compile");
      }

      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      Objects.requireNonNull(compiler);
      JavaCompiler.CompilationTask compilerTask = compiler.getTask(null,
                                                                   null,
                                                                   null,
                                                                   options,
                                                                   compiledClassNames,
                                                                   toCompile);

      compilerTask.setProcessors(Collections.singletonList(createProcessor()));
      compilerTask.call();
   }

   private Processor createProcessor()
   {
      return new ShadowProcessor()
      {
         @Override
         public void process(ShadowApi shadowApi)
         {
            processingCallback.process(shadowApi);
         }
      };
   }

   private SimpleJavaFileObject createJavaFileObject(URI uri, String sourceCode)
   {
      return new SimpleJavaFileObject(uri, JavaFileObject.Kind.SOURCE)
      {
         @Override
         public CharSequence getCharContent(boolean b)
         {
            return sourceCode;
         }
      };
   }

   private static URI createUri(String fileName)
   {
      return URI.create(fileName);
   }
}