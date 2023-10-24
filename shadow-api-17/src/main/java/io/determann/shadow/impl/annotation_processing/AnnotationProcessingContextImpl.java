package io.determann.shadow.impl.annotation_processing;

import io.determann.shadow.api.*;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import static io.determann.shadow.api.MirrorAdapter.*;
import static io.determann.shadow.api.converter.Converter.convert;
import static java.lang.System.err;
import static java.lang.System.out;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public class AnnotationProcessingContextImpl implements AnnotationProcessingContext
{
   private ProcessingEnvironment processingEnv;
   private RoundEnvironment roundEnv;
   private int processingRound;
   private BiConsumer<AnnotationProcessingContext, Throwable> exceptionHandler = (shadowApi, throwable) ->
   {
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      throwable.printStackTrace(printWriter);
      logError(stringWriter.toString());
      throw new RuntimeException(throwable);
   };
   private BiConsumer<AnnotationProcessingContext, DiagnosticContext> diagnosticHandler = (shadowApi, diagnosticContext) ->
   {
      if (!shadowApi.isProcessingOver())
      {
         String duration = Duration.between(diagnosticContext.getStart(), diagnosticContext.getEnd()).toString()
                                   .substring(2)
                                   .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                                   .toLowerCase();

         logInfo(diagnosticContext.getProcessorName() +
                 " took " +
                 duration +
                 " in round " +
                 diagnosticContext.getProcessingRound() +
                 "\n");
      }
   };
   private BiConsumer<AnnotationProcessingContext, String> systemOutHandler = (shadowApi, s) ->
   {
      if (!getProcessingEnv().toString().startsWith("javac"))
      {
         logWarning(s);
      }
   };
   private BiConsumer<AnnotationProcessingContext, String> systemErrorHandler = AnnotationProcessingContext::logError;


   public AnnotationProcessingContextImpl(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, int processingRound)
   {
      update(processingEnv, roundEnv, processingRound);

      proxySystemOut();
      proxySystemErr();
   }

   public AnnotationProcessingContext update(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, int processingRound)
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
               systemOutHandler.accept(AnnotationProcessingContextImpl.this, x);
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
               systemErrorHandler.accept(AnnotationProcessingContextImpl.this, x);
            }
         }
      };
      System.setErr(printStream);
   }

   @Override
   public AnnotationTypeChooser getAnnotatedWith(String qualifiedAnnotation)
   {
      TypeElement annotation = getProcessingEnv().getElementUtils().getTypeElement(qualifiedAnnotation);
      if (annotation == null || !annotation.getKind().equals(ElementKind.ANNOTATION_TYPE))
      {
         throw new IllegalArgumentException("No annotation found with qualified name \"" + qualifiedAnnotation + "\"");
      }
      return new AnnotationTypeChooserImpl(this, getRoundEnv().getElementsAnnotatedWith(annotation));
   }

   @Override
   public AnnotationTypeChooser getAnnotatedWith(Annotation annotation)
   {
      return getAnnotatedWith(annotation.getQualifiedName());
   }

   @Override
   public List<Module> getModules()
   {
      return getProcessingEnv().getElementUtils()
                          .getAllModuleElements()
                          .stream()
                          .map(moduleElement -> MirrorAdapter.<Module>getShadow(this, moduleElement))
                          .toList();
   }

   @Override
   public Optional<Module> getModule(String name)
   {
      return ofNullable(getProcessingEnv().getElementUtils().getModuleElement(name))
            .map(moduleElement -> getShadow(this, moduleElement));
   }

   @Override
   public Module getModuleOrThrow(String name)
   {
      return getModule(name).orElseThrow();
   }

   @Override
   public List<Package> getPackages(String qualifiedName)
   {
      return getProcessingEnv().getElementUtils()
                          .getAllPackageElements(qualifiedName)
                          .stream()
                          .map(packageElement -> MirrorAdapter.<Package>getShadow(this, packageElement))
                          .toList();
   }

   @Override
   public List<Package> getPackages()
   {
      return getProcessingEnv().getElementUtils()
                          .getAllModuleElements()
                          .stream()
                          .flatMap(moduleElement -> moduleElement.getEnclosedElements().stream())
                          .map(packageElement -> MirrorAdapter.<Package>getShadow(this, packageElement))
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
      return ofNullable(getProcessingEnv().getElementUtils()
                                     .getPackageElement(getElement(module),
                                                        qualifiedPackageName))
            .map(packageElement -> getShadow(this, packageElement));
   }

   @Override
   public Package getPackageOrThrow(Module module, String qualifiedPackageName)
   {
      return getPackage(module, qualifiedPackageName).orElseThrow();
   }

   @Override
   public Optional<Declared> getDeclared(String qualifiedName)
   {
      return ofNullable(getProcessingEnv().getElementUtils().getTypeElement(qualifiedName))
            .map(typeElement -> getShadow(this, typeElement));
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
   public ShadowConstants getConstants()
   {
      return new ShadowConstantsImpl(this);
   }

   @Override
   public void writeSourceFile(String qualifiedName, String content)
   {
      try (Writer writer = getProcessingEnv().getFiler().createSourceFile(qualifiedName).openWriter())
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
      try (Writer writer = getProcessingEnv().getFiler().createClassFile(qualifiedName).openWriter())
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
      try (Writer writer = getProcessingEnv().getFiler().createResource(location, moduleAndPkg, relativPath).openWriter())
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
      return getProcessingEnv().getFiler().getResource(location, moduleAndPkg, relativPath);
   }

   @Override
   public boolean isProcessingOver()
   {
      return getRoundEnv().processingOver();
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

   @Override
   public Declared erasure(Class aClass)
   {
      return erasureImpl(getType(aClass));
   }

   @Override
   public Declared erasure(Interface anInterface)
   {
      return erasureImpl(getType(anInterface));
   }

   @Override
   public Declared erasure(Record aRecord)
   {
      return erasureImpl(getType(aRecord));
   }

   @Override
   public Array erasure(Array array)
   {
      return erasureImpl(getType(array));
   }

   @Override
   public Shadow erasure(Wildcard wildcard)
   {
      return erasureImpl(getType(wildcard));
   }

   @Override
   public Shadow erasure(Generic generic)
   {
      return erasureImpl(getType(generic));
   }

   @Override
   public Shadow erasure(Intersection intersection)
   {
      return erasureImpl(getType(intersection));
   }

   @Override
   public RecordComponent erasure(RecordComponent recordComponent)
   {
      return erasureImpl(getType(recordComponent));
   }

   @Override
   public Shadow erasure(Parameter parameter)
   {
      return erasureImpl(getType(parameter));
   }

   @Override
   public Shadow erasure(Field field)
   {
      return erasureImpl(getType(field));
   }

   private <S extends Shadow> S erasureImpl(TypeMirror typeMirror)
   {
      return MirrorAdapter.getShadow(this, getProcessingEnv().getTypeUtils().erasure(typeMirror));
   }

   @Override
   public Class interpolateGenerics(Class aClass)
   {
      return MirrorAdapter.getShadow(getApi(), getProcessingEnv().getTypeUtils().capture(getType(aClass)));
   }

   @Override
   public Interface interpolateGenerics(Interface anInterface)
   {
      return MirrorAdapter.getShadow(getApi(), getProcessingEnv().getTypeUtils().capture(getType(anInterface)));
   }

   @Override
   public Record interpolateGenerics(Record aRecord)
   {
      return MirrorAdapter.getShadow(getApi(), getProcessingEnv().getTypeUtils().capture(getType(aRecord)));
   }

   @Override
   public void setExceptionHandler(BiConsumer<AnnotationProcessingContext, Throwable> exceptionHandler)
   {
      this.exceptionHandler = exceptionHandler;
   }

   @Override
   public BiConsumer<AnnotationProcessingContext, Throwable> getExceptionHandler()
   {
      return exceptionHandler;
   }

   @Override
   public void setDiagnosticHandler(BiConsumer<AnnotationProcessingContext, DiagnosticContext> diagnosticHandler)
   {
      this.diagnosticHandler = diagnosticHandler;
   }

   @Override
   public BiConsumer<AnnotationProcessingContext, DiagnosticContext> getDiagnosticHandler()
   {
      return diagnosticHandler;
   }

   @Override
   public void setSystemOutHandler(BiConsumer<AnnotationProcessingContext, String> systemOutHandler)
   {
      this.systemOutHandler = systemOutHandler;
   }

   @Override
   public BiConsumer<AnnotationProcessingContext, String> getSystemOutHandler()
   {
      return systemOutHandler;
   }

   @Override
   public void setSystemErrorHandler(BiConsumer<AnnotationProcessingContext, String> systemErrorHandler)
   {
      this.systemErrorHandler = systemErrorHandler;
   }

   @Override
   public BiConsumer<AnnotationProcessingContext, String> getSystemErrorHandler()
   {
      return systemErrorHandler;
   }

   @Override
   public Class withGenerics(Class aClass, Shadow... generics)
   {
      if (generics.length == 0 || aClass.getFormalGenerics().size() != generics.length)
      {
         throw new IllegalArgumentException(aClass.getQualifiedName() +
                                            " has " +
                                            aClass.getFormalGenerics().size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      if (aClass.getOuterType().flatMap(typeMirrorShadow -> convert(typeMirrorShadow)
                      .toInterface()
                      .map(anInterface -> !anInterface.getFormalGenerics().isEmpty())
                      .or(() -> convert(typeMirrorShadow).toClass().map(aClass1 -> !aClass1.getGenerics().isEmpty())))
                .orElse(false))
      {
         throw new IllegalArgumentException("cant add generics to " +
                                            aClass.getQualifiedName() +
                                            " when the class is not static and the outer class has generics");
      }
      TypeMirror[] typeMirrors = Arrays.stream(generics)
                                       .map(MirrorAdapter::getType)
                                       .toArray(TypeMirror[]::new);

      return getShadow(this, getProcessingEnv().getTypeUtils().getDeclaredType(getElement(aClass), typeMirrors));
   }

   @Override
   public Interface withGenerics(Interface anInterface, Shadow... generics)
   {
      if (generics.length == 0 || anInterface.getFormalGenerics().size() != generics.length)
      {
         throw new IllegalArgumentException(anInterface.getQualifiedName() +
                                            " has " +
                                            anInterface.getFormalGenerics().size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      TypeMirror[] typeMirrors = Arrays.stream(generics)
                                       .map(MirrorAdapter::getType)
                                       .toArray(TypeMirror[]::new);

      return getShadow(this, getProcessingEnv().getTypeUtils().getDeclaredType(getElement(anInterface), typeMirrors));
   }

   @Override
   public Record withGenerics(Record aRecord, Shadow... generics)
   {
      if (generics.length == 0 || aRecord.getFormalGenerics().size() != generics.length)
      {
         throw new IllegalArgumentException(aRecord.getQualifiedName() +
                                            " has " +
                                            aRecord.getFormalGenerics().size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(MirrorAdapter::getType)
            .toArray(TypeMirror[]::new);

      return getShadow(this, getProcessingEnv().getTypeUtils().getDeclaredType(getElement(aRecord), typeMirrors));
   }

   @Override
   public void logError(String msg)
   {
      MirrorAdapter.getProcessingEnv(this).getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
   }

   @Override
   public void logInfo(String msg)
   {
      MirrorAdapter.getProcessingEnv(this).getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
   }

   @Override
   public void logWarning(String msg)
   {
      MirrorAdapter.getProcessingEnv(this).getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg);
   }

   @Override
   public void logErrorAt(Annotationable annotationable, String msg)
   {
      MirrorAdapter.getProcessingEnv(this).getMessager().printMessage(Diagnostic.Kind.ERROR, msg, getElement(annotationable));
   }

   @Override
   public void logInfoAt(Annotationable annotationable, String msg)
   {
      MirrorAdapter.getProcessingEnv(this).getMessager().printMessage(Diagnostic.Kind.NOTE, msg, getElement(annotationable));
   }

   @Override
   public void logWarningAt(Annotationable annotationable, String msg)
   {
      MirrorAdapter.getProcessingEnv(this).getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg, getElement(annotationable));
   }

   @Override
   public AnnotationProcessingContext getApi()
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
