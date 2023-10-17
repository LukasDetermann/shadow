package io.determann.shadow.impl.test;

import io.determann.shadow.api.ShadowProcessor;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.test.ProcessingCallback;
import io.determann.shadow.api.test.ProcessorTest;

import javax.annotation.processing.Processor;
import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ProcessorTestImpl implements ProcessorTest
{
   private final ProcessingCallback processingCallback;
   private final List<JavaFileObject> toCompile;
   private final List<String> options;
   private final List<String> compiledClassNames;

   private ProcessorTestImpl(ProcessingCallback processingCallback,
                             List<JavaFileObject> toCompile,
                             List<String> options,
                             List<String> compiledClassNames)
   {
      this.processingCallback = processingCallback;
      this.toCompile = toCompile;
      this.options = options;
      this.compiledClassNames = compiledClassNames;
   }

   public ProcessorTestImpl(ProcessingCallback processingCallback)
   {
      this.processingCallback = processingCallback;
      toCompile = new ArrayList<>();
      options = new ArrayList<>();
      compiledClassNames = new ArrayList<>();
      compiledClassNames.add(Object.class.getName());
   }

   @Override
   public ProcessorTest withCodeToCompile(File fileToCompile)
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
      return new ProcessorTestImpl(processingCallback, toCompile, options, compiledClassNames);
   }

   @Override
   public ProcessorTest withCodeToCompile(String fileName, String code)
   {
      toCompile.add(createJavaFileObject(createUri(fileName), code));
      return new ProcessorTestImpl(processingCallback, toCompile, options, compiledClassNames);
   }

   @Override
   public ProcessorTest withOption(String option)
   {
      this.options.add(option);
      return new ProcessorTestImpl(processingCallback, toCompile, options, compiledClassNames);
   }

   @Override
   public ProcessorTest withCompiledClass(String qualifiedName)
   {
      this.compiledClassNames.add(qualifiedName);
      return new ProcessorTestImpl(processingCallback, toCompile, options, compiledClassNames);
   }

   @Override
   public ProcessorTest withCompiledClass(Class<?> aClass)
   {
      this.compiledClassNames.add(aClass.getName());
      return new ProcessorTestImpl(processingCallback, toCompile, options, compiledClassNames);
   }

   @Override
   public void compile()
   {
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      Objects.requireNonNull(compiler);
      JavaCompiler.CompilationTask compilerTask = compiler.getTask(null,
                                                                   new NonWritingFileManager(compiler.getStandardFileManager(null, null, null)),
                                                                   diagnostic ->
                                                                   {
                                                                      if (diagnostic.getKind().equals(Diagnostic.Kind.ERROR))
                                                                      {
                                                                         throw new IllegalStateException(diagnostic.toString());
                                                                      }
                                                                   },
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
         public void process(AnnotationProcessingContext annotationProcessingContext)
         {
            processingCallback.process(annotationProcessingContext);
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

   private URI createUri(String fileName)
   {
      try
      {
         return processingCallback.getClass()
                                  .getProtectionDomain()
                                  .getCodeSource()
                                  .getLocation()
                                  .toURI()
                                  .resolve(fileName);
      }
      catch (SecurityException | URISyntaxException e)
      {
         return URI.create(fileName);
      }
   }
}