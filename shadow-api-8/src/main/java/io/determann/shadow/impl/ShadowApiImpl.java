package io.determann.shadow.impl;

import io.determann.shadow.api.*;
import io.determann.shadow.api.converter.DeclaredMapper;
import io.determann.shadow.api.renderer.NameRenderedEvent;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.impl.renderer.Context;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static java.lang.System.err;
import static java.lang.System.out;
import static java.util.Optional.ofNullable;

public class ShadowApiImpl implements ShadowApi
{
   private ProcessingEnvironment processingEnv;
   private RoundEnvironment roundEnv;
   private int processingRound;
   private Context renderingContext = Context.builder().withMostlyQualifiedNames().build();
   private BiConsumer<ShadowApi, Throwable> exceptionHandler = (shadowApi, throwable) ->
   {
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      throwable.printStackTrace(printWriter);
      shadowApi.logError(stringWriter.toString());
      throw new RuntimeException(throwable);
   };
   private BiConsumer<ShadowApi, DiagnosticContext> diagnosticHandler = (shadowApi, diagnosticContext) ->
   {
      if (!shadowApi.isProcessingOver())
      {
         String duration = Duration.between(diagnosticContext.getStart(), diagnosticContext.getEnd()).toString()
                                   .substring(2)
                                   .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                                   .toLowerCase();

         shadowApi.logInfo(diagnosticContext.getProcessorName() +
                           " took " +
                           duration +
                           " in round " +
                           diagnosticContext.getProcessingRound() +
                           "\n");
      }
   };
   private BiConsumer<ShadowApi, String> systemOutHandler = (shadowApi, s) ->
   {
      if (!MirrorAdapter.getProcessingEnv(getApi()).toString().startsWith("javac"))
      {
         shadowApi.logWarning(s);
      }
   };
   private BiConsumer<ShadowApi, String> systemErrorHandler = ShadowApi::logError;


   public ShadowApiImpl(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, int processingRound)
   {
      update(processingEnv, roundEnv, processingRound);

      proxySystemOut();
      proxySystemErr();
   }

   public ShadowApi update(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, int processingRound)
   {
      this.processingRound = processingRound;
      this.processingEnv = processingEnv;
      this.roundEnv = roundEnv;
      return this;
   }

   private void proxySystemOut()
   {
      //in >= java 18 out.getCharset()
      PrintStream printStream = new PrintStream(out)
      {
         @Override
         public void println(String x)
         {
            super.println(x);
            if (x != null && systemOutHandler != null)
            {
               systemOutHandler.accept(ShadowApiImpl.this, x);
            }
         }
      };
      System.setOut(printStream);
   }

   private void proxySystemErr()
   {
      //in >= java 18 out.getCharset()
      PrintStream printStream = new PrintStream(err)
      {
         @Override
         public void println(String x)
         {
            super.println(x);
            if (x != null && systemErrorHandler != null)
            {
               systemErrorHandler.accept(ShadowApiImpl.this, x);
            }
         }
      };
      System.setErr(printStream);
   }

   @Override
   public AnnotationTypeChooser getAnnotatedWith(String qualifiedAnnotation)
   {
      TypeElement annotation = MirrorAdapter.getProcessingEnv(getApi()).getElementUtils().getTypeElement(qualifiedAnnotation);
      if (annotation == null || !annotation.getKind().equals(ElementKind.ANNOTATION_TYPE))
      {
         throw new IllegalArgumentException("No annotation found with qualified name \"" + qualifiedAnnotation + "\"");
      }
      return new AnnotationTypeChooserImpl(this, MirrorAdapter.getRoundEnv(getApi()).getElementsAnnotatedWith(annotation));
   }

   @Override
   public AnnotationTypeChooser getAnnotatedWith(Annotation annotation)
   {
      return getAnnotatedWith(annotation.getQualifiedName());
   }

   @Override
   public Optional<Package> getPackage(String qualifiedName)
   {
      return Optional.ofNullable(MirrorAdapter.getProcessingEnv(getApi()).getElementUtils().getPackageElement(qualifiedName))
                     .map(packageElement -> MirrorAdapter.getShadow(getApi(), packageElement));
   }

   @Override
   public Package getPackageOrThrow(String qualifiedName)
   {
      return getPackage(qualifiedName).orElseThrow(NoSuchElementException::new);
   }

   @Override
   public Optional<Declared> getDeclared(String qualifiedName)
   {
      return ofNullable(MirrorAdapter.getProcessingEnv(getApi()).getElementUtils().getTypeElement(qualifiedName))
            .map(typeElement -> MirrorAdapter.getShadow(getApi(), typeElement));
   }

   @Override
   public ShadowConstants getConstants()
   {
      return new ShadowConstantsImpl(this);
   }

   @Override
   public void logError(String msg)
   {
      MirrorAdapter.getProcessingEnv(getApi()).getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
   }

   @Override
   public void logInfo(String msg)
   {
      MirrorAdapter.getProcessingEnv(getApi()).getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
   }

   @Override
   public void logWarning(String msg)
   {
      MirrorAdapter.getProcessingEnv(getApi()).getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg);
   }

   @Override
   public void logErrorAt(Annotationable elementBacked, String msg)
   {
      elementBacked.logError(msg);
   }

   @Override
   public void logInfoAt(Annotationable elementBacked, String msg)
   {
      elementBacked.logInfo(msg);
   }

   @Override
   public void logWarningAt(Annotationable elementBacked, String msg)
   {
      elementBacked.logWarning(msg);
   }

   @Override
   public void writeSourceFile(String qualifiedName, String content)
   {
      try (Writer writer = MirrorAdapter.getProcessingEnv(getApi()).getFiler().createSourceFile(qualifiedName).openWriter())
      {
         writer.write(content);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public void writeClassFile(String qualifiedName, String content)
   {
      try (Writer writer = MirrorAdapter.getProcessingEnv(getApi()).getFiler().createClassFile(qualifiedName).openWriter())
      {
         writer.write(content);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public void writeResource(StandardLocation location, String moduleAndPkg, String relativPath, String content)
   {
      try (Writer writer = MirrorAdapter.getProcessingEnv(getApi()).getFiler().createResource(location, moduleAndPkg, relativPath).openWriter())
      {
         writer.write(content);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public FileObject readResource(StandardLocation location, String moduleAndPkg, String relativPath) throws IOException
   {
      return MirrorAdapter.getProcessingEnv(getApi()).getFiler().getResource(location, moduleAndPkg, relativPath);
   }

   @Override
   public boolean isProcessingOver()
   {
      return MirrorAdapter.getRoundEnv(getApi()).processingOver();
   }

   @Override
   public boolean isFirstRound()
   {
      return processingRound == 0;
   }

   @Override
   public int getProcessingRound()
   {
      return processingRound;
   }

   public static Declared erasure(Declared declared)
   {
      return ShadowApi.convert(declared).map(new DeclaredMapper<Declared>()
      {
         @Override
         public Declared annotationType(Annotation annotation)
         {
            return annotation;
         }

         @Override
         public Declared enumType(Enum aEnum)
         {
            return aEnum;
         }

         @Override
         public Declared classType(Class aClass)
         {
            return aClass.erasure();
         }

         @Override
         public Declared interfaceType(Interface aInterface)
         {
            return aInterface.erasure();
         }
      });
   }

   @Override
   public void setExceptionHandler(BiConsumer<ShadowApi, Throwable> exceptionHandler)
   {
      this.exceptionHandler = exceptionHandler;
   }

   @Override
   public BiConsumer<ShadowApi, Throwable> getExceptionHandler()
   {
      return exceptionHandler;
   }

   @Override
   public void setDiagnosticHandler(BiConsumer<ShadowApi, DiagnosticContext> diagnosticHandler)
   {
      this.diagnosticHandler = diagnosticHandler;
   }

   @Override
   public BiConsumer<ShadowApi, DiagnosticContext> getDiagnosticHandler()
   {
      return diagnosticHandler;
   }

   @Override
   public void setSystemOutHandler(BiConsumer<ShadowApi, String> systemOutHandler)
   {
      this.systemOutHandler = systemOutHandler;
   }

   @Override
   public BiConsumer<ShadowApi, String> getSystemOutHandler()
   {
      return systemOutHandler;
   }

   @Override
   public void setSystemErrorHandler(BiConsumer<ShadowApi, String> systemErrorHandler)
   {
      this.systemErrorHandler = systemErrorHandler;
   }

   @Override
   public BiConsumer<ShadowApi, String> getSystemErrorHandler()
   {
      return systemErrorHandler;
   }

   @Override
   public void renderQualifiedNames()
   {
      renderingContext = Context.builder(renderingContext).withQualifiedNames().build();
   }

   @Override
   public void renderSimpleNames()
   {
      renderingContext = Context.builder(renderingContext).withSimpleNames().build();
   }

   @Override
   public void renderNamesWithoutNeedingImports()
   {
      renderingContext = Context.builder(renderingContext).withMostlyQualifiedNames().build();
   }

   @Override
   public void onNameRendered(Consumer<NameRenderedEvent> onNameRendered)
   {
      renderingContext = Context.builder(renderingContext).withNameRenderedListener(onNameRendered).build();
   }

   public Context getRenderingContext()
   {
      return renderingContext;
   }

   @Override
   public ShadowApi getApi()
   {
      return this;
   }

   public ProcessingEnvironment getProcessingEnv()
   {
      return processingEnv;
   }

   public RoundEnvironment getRoundEnv()
   {
      return roundEnv;
   }
}
