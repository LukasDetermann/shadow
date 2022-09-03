package org.determann.shadow.impl.test;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.ShadowProcessor;
import org.determann.shadow.api.test.CompilationTestBuilder;
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

public class CompilationTestBuilderImpl implements CompilationTestBuilder
{
   private final ProcessingCallback processingCallback;
   private final List<JavaFileObject> toCompile = new ArrayList<>();
   private final List<String> options = new ArrayList<>();
   private final List<String> compiledClassNames = new ArrayList<>();

   public CompilationTestBuilderImpl(ProcessingCallback processingCallback)
   {
      this.processingCallback = processingCallback;
   }

   @Override
   public CompilationTestBuilder withFileToCompile(File fileToCompile)
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
      return this;
   }

   @Override
   public CompilationTestBuilder withFileToCompile(String fileName, String sourceCode)
   {
      toCompile.add(createJavaFileObject(createUri(fileName), sourceCode));
      return this;
   }

   @Override
   public CompilationTestBuilder withOption(String option)
   {
      this.options.add(option);
      return this;
   }

   @Override
   public CompilationTestBuilder withCompiledClassName(String withCompiledClassName)
   {
      this.compiledClassNames.add(withCompiledClassName);
      return this;
   }

   @Override
   public CompilationTestBuilder withCompiledClassName(Class<?> withCompiledClass)
   {
      this.compiledClassNames.add(withCompiledClass.getName());
      return this;
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