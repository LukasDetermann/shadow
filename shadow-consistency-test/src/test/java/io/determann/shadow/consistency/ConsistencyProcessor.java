package io.determann.shadow.consistency;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.annotation_processing.ShadowProcessor;
import io.determann.shadow.api.annotation_processing.test.ProcessingCallback;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConsistencyProcessor
{
   private final ProcessingCallback processingCallback;
   private final List<JavaFileObject> toCompile;

   public ConsistencyProcessor(ProcessingCallback processingCallback,
                               List<JavaFileObject> toCompile)
   {
      this.processingCallback = processingCallback;
      this.toCompile = toCompile;
   }

   public Map<String, byte[]> compile()
   {
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      Objects.requireNonNull(compiler);
      ConsistencyFileManager fileManager = new ConsistencyFileManager(compiler.getStandardFileManager(null, null, null));
      JavaCompiler.CompilationTask compilerTask = compiler.getTask(null,
                                                                   fileManager,
                                                                   diagnostic ->
                                                                   {
                                                                      if (diagnostic.getKind().equals(Diagnostic.Kind.ERROR))
                                                                      {
                                                                         throw new IllegalStateException(diagnostic.toString());
                                                                      }
                                                                   },
                                                                   null,
                                                                   Collections.singletonList(Object.class.getName()),
                                                                   toCompile);

      compilerTask.setProcessors(Collections.singletonList(createProcessor()));
      compilerTask.call();
      return fileManager.getCompiledClasses();
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
}