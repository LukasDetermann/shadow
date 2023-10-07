package io.determann.shadow.impl;

import io.determann.shadow.api.*;
import io.determann.shadow.api.converter.DeclaredMapper;
import io.determann.shadow.api.renderer.NameRenderedEvent;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
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
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static java.lang.System.err;
import static java.lang.System.out;
import static java.util.Optional.ofNullable;

public class ShadowApiImpl implements ShadowApi
{
   private JdkApiContext jdkApiContext;
   private final ShadowFactory shadowFactory = new ShadowFactoryImpl(this);
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
      if (!shadowApi.getJdkApiContext().getProcessingEnv().toString().startsWith("javac"))
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
      this.jdkApiContext = new JdkApiContext(processingEnv, roundEnv);
      return this;
   }

   @Override
   public JdkApiContext getJdkApiContext()
   {
      return jdkApiContext;
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
      TypeElement annotation = getJdkApiContext().getProcessingEnv().getElementUtils().getTypeElement(qualifiedAnnotation);
      if (annotation == null || !annotation.getKind().equals(ElementKind.ANNOTATION_TYPE))
      {
         throw new IllegalArgumentException("No annotation found with qualified name \"" + qualifiedAnnotation + "\"");
      }
      return new AnnotationTypeChooserImpl(this, jdkApiContext.getRoundEnv().getElementsAnnotatedWith(annotation));
   }

   @Override
   public AnnotationTypeChooser getAnnotatedWith(Annotation annotation)
   {
      return getAnnotatedWith(annotation.getQualifiedName());
   }

   @Override
   public List<Module> getModules()
   {
      return getJdkApiContext().getProcessingEnv().getElementUtils()
                               .getAllModuleElements()
                               .stream()
                               .map(moduleElement -> getShadowFactory().<Module>shadowFromElement(moduleElement))
                               .toList();
   }

   @Override
   public Optional<Module> getModule(String name)
   {
      return ofNullable(getJdkApiContext().getProcessingEnv().getElementUtils().getModuleElement(name))
            .map(moduleElement -> getShadowFactory().shadowFromElement(moduleElement));
   }

   @Override
   public Module getModuleOrThrow(String name)
   {
      return getModule(name).orElseThrow();
   }

   @Override
   public List<Package> getPackages(String qualifiedName)
   {
      return getJdkApiContext().getProcessingEnv().getElementUtils()
                               .getAllPackageElements(qualifiedName)
                               .stream()
                               .map(packageElement -> getShadowFactory().<Package>shadowFromElement(packageElement))
                               .toList();
   }

   @Override
   public List<Package> getPackages()
   {
      return getJdkApiContext().getProcessingEnv().getElementUtils()
                               .getAllModuleElements()
                               .stream()
                               .flatMap(moduleElement -> moduleElement.getEnclosedElements().stream())
                               .map(packageElement -> getShadowFactory().<Package>shadowFromElement(packageElement))
                               .toList();
   }

   @Override
   public Optional<Package> getPackage(String qualifiedModuleName, String qualifiedPackageName)
   {
      return getPackage(getModuleOrThrow(qualifiedModuleName), qualifiedPackageName);
   }

   @Override
   public Package getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName)
   {
      return getPackage(qualifiedModuleName, qualifiedPackageName).orElseThrow();
   }

   @Override
   public Optional<Package> getPackage(Module module, String qualifiedPackageName)
   {
      return ofNullable(getJdkApiContext().getProcessingEnv().getElementUtils()
                                          .getPackageElement(module.getElement(),
                                                             qualifiedPackageName))
            .map(packageElement -> getShadowFactory().shadowFromElement(packageElement));
   }

   @Override
   public Package getPackageOrThrow(Module module, String qualifiedPackageName)
   {
      return getPackage(module, qualifiedPackageName).orElseThrow();
   }

   @Override
   public Optional<Declared> getDeclared(String qualifiedName)
   {
      return ofNullable(getJdkApiContext().getProcessingEnv().getElementUtils().getTypeElement(qualifiedName))
            .map(typeElement -> getShadowFactory().shadowFromElement(typeElement));
   }

   @Override
   public List<Declared> getDeclared()
   {
      return getPackages()
            .stream()
            .flatMap(packageShadow -> packageShadow.getContent().stream())
            .toList();
   }

   @Override
   public ShadowFactory getShadowFactory()
   {
      return shadowFactory;
   }

   @Override
   public ShadowConstants getConstants()
   {
      return new ShadowConstantsImpl(this);
   }

   @Override
   public void logError(String msg)
   {
      getJdkApiContext().getProcessingEnv().getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
   }

   @Override
   public void logInfo(String msg)
   {
      getJdkApiContext().getProcessingEnv().getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
   }

   @Override
   public void logWarning(String msg)
   {
      getJdkApiContext().getProcessingEnv().getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg);
   }

   @Override
   public void logErrorAt(Annotationable<?> elementBacked, String msg)
   {
      elementBacked.logError(msg);
   }

   @Override
   public void logInfoAt(Annotationable<?> elementBacked, String msg)
   {
      elementBacked.logInfo(msg);
   }

   @Override
   public void logWarningAt(Annotationable<?> elementBacked, String msg)
   {
      elementBacked.logWarning(msg);
   }

   @Override
   public void writeSourceFile(String qualifiedName, String content)
   {
      try (Writer writer = getJdkApiContext().getProcessingEnv().getFiler().createSourceFile(qualifiedName).openWriter())
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
      try (Writer writer = getJdkApiContext().getProcessingEnv().getFiler().createClassFile(qualifiedName).openWriter())
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
      try (Writer writer = getJdkApiContext().getProcessingEnv().getFiler().createResource(location, moduleAndPkg, relativPath).openWriter())
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
      return getJdkApiContext().getProcessingEnv().getFiler().getResource(location, moduleAndPkg, relativPath);
   }

   @Override
   public boolean isProcessingOver()
   {
      return getJdkApiContext().getRoundEnv().processingOver();
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
      return ShadowApi.convert(declared).map(new DeclaredMapper<>()
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

         @Override
         public Declared recordType(Record aRecord)
         {
            return aRecord.erasure();
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
   public ShadowApi getApi()
   {
      return this;
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
}
