package io.determann.shadow.internal.annotation_processing;

import io.determann.shadow.api.C;
import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.Constants;
import io.determann.shadow.api.annotation_processing.DiagnosticContext;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static io.determann.shadow.api.query.Operations.PACKAGE_GET_DECLARED_LIST;
import static io.determann.shadow.api.query.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static java.lang.System.out;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

public class ApContextImpl
      implements Ap.Context
{
   public static final String IMPLEMENTATION_NAME = "io.determann.shadow-annotation-processing";
   private final Implementation implementation;

   private final ProcessingEnvironment processingEnv;
   private final RoundEnvironment roundEnv;
   private final int processingRound;
   private final Types types;
   private final Elements elements;
   private BiConsumer<Ap.Context, Throwable> exceptionHandler = (context, throwable) ->
   {
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      throwable.printStackTrace(printWriter);
      logAndRaiseError(stringWriter.toString());
      throw new RuntimeException(throwable);
   };
   private BiConsumer<Ap.Context, DiagnosticContext> diagnosticHandler = (context, diagnosticContext) ->
   {
      if (!context.isProcessingOver())
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
   private BiConsumer<Ap.Context, String> systemOutHandler = (context, s) ->
   {
      if (!getProcessingEnv().toString().startsWith("javac"))
      {
         logWarning(s);
      }
   };

   public ApContextImpl(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, int processingRound)
   {
      types = processingEnv.getTypeUtils();
      elements = processingEnv.getElementUtils();
      implementation = new ApImplementation(IMPLEMENTATION_NAME, this);
      this.processingRound = processingRound;
      this.processingEnv = processingEnv;
      this.roundEnv = roundEnv;

      proxySystemOut();
   }

   private void proxySystemOut()
   {
      PrintStream printStream = new PrintStream(out, false, out.charset())
      {
         @Override
         public void println(String x)
         {
            super.println(x);
            if (x != null && systemOutHandler != null)
            {
               systemOutHandler.accept(ApContextImpl.this, x);
            }
         }
      };
      System.setOut(printStream);
   }

   private <TYPE> Set<TYPE> getAnnotated(String qualifiedAnnotation, java.lang.Class<TYPE> typeClass)
   {
      TypeElement annotation = getProcessingEnv().getElementUtils().getTypeElement(qualifiedAnnotation);
      if (annotation == null || !annotation.getKind().equals(ElementKind.ANNOTATION_TYPE))
      {
         throw new IllegalArgumentException("No annotation found with qualified name \"" + qualifiedAnnotation + "\"");
      }
      return getAnnotated(annotation, typeClass);
   }

   private <TYPE> Set<TYPE> getAnnotated(TypeElement annotation, java.lang.Class<TYPE> typeClass)
   {
      return getRoundEnv().getElementsAnnotatedWith(annotation).stream()
                          .map(element ->
                               {
                                  if (element.getKind().isExecutable())
                                  {
                                     return (C.Annotationable) Adapters.adapt(getApi(), ((ExecutableElement) element));
                                  }
                                  return ((C.Annotationable) Adapters.adapt(getApi(), element));
                               })
                          .filter(typeClass::isInstance)
                          .map(typeClass::cast)
                          .collect(toSet());
   }

   private <RESULT> Set<RESULT> getAnnotated(C.QualifiedNameable input, java.lang.Class<RESULT> resultClass)
   {
      if (input instanceof Ap.Annotation annotationLangModel)
      {
         return getAnnotated(adapt(annotationLangModel).toTypeElement(), resultClass);
      }
      return getAnnotated(requestOrThrow(input, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME), resultClass);
   }

   @Override
   public Set<Ap.Annotationable> getAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, Ap.Annotationable.class);
   }

   @Override
   public Set<Ap.Annotationable> getAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, Ap.Annotationable.class);
   }

   @Override
   public Set<Ap.Declared> getDeclaredAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, Ap.Declared.class);
   }

   @Override
   public Set<Ap.Declared> getDeclaredAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, Ap.Declared.class);
   }

   @Override
   public Set<Ap.Class> getClassesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, Ap.Class.class);
   }

   @Override
   public Set<Ap.Class> getClassesAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, Ap.Class.class);
   }

   @Override
   public Set<Ap.Enum> getEnumsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, Ap.Enum.class);
   }

   @Override
   public Set<Ap.Enum> getEnumsAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, Ap.Enum.class);
   }

   @Override
   public Set<Ap.Interface> getInterfacesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, Ap.Interface.class);
   }

   @Override
   public Set<Ap.Interface> getInterfacesAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, Ap.Interface.class);
   }

   @Override
   public Set<Ap.Record> getRecordsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, Ap.Record.class);
   }

   @Override
   public Set<Ap.Record> getRecordsAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, Ap.Record.class);
   }

   @Override
   public Set<Ap.Field> getFieldsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, Ap.Field.class);
   }

   @Override
   public Set<Ap.Field> getFieldsAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, Ap.Field.class);
   }

   @Override
   public Set<Ap.Parameter> getParametersAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, Ap.Parameter.class);
   }

   @Override
   public Set<Ap.Parameter> getParametersAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, Ap.Parameter.class);
   }

   @Override
   public Set<Ap.Method> getMethodsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, Ap.Method.class);
   }

   @Override
   public Set<Ap.Method> getMethodsAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, Ap.Method.class);
   }

   @Override
   public Set<Ap.Constructor> getConstructorsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, Ap.Constructor.class);
   }

   @Override
   public Set<Ap.Constructor> getConstructorsAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, Ap.Constructor.class);
   }

   @Override
   public Set<Ap.Annotation> getAnnotationsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, Ap.Annotation.class);
   }

   @Override
   public Set<Ap.Annotation> getAnnotationsAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, Ap.Annotation.class);
   }

   @Override
   public Set<Ap.Package> getPackagesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, Ap.Package.class);
   }

   @Override
   public Set<Ap.Package> gePackagesAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, Ap.Package.class);
   }

   @Override
   public Set<Ap.Generic> getGenericDeclarationsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, Ap.Generic.class);
   }

   @Override
   public Set<Ap.Generic> geGenericsAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, Ap.Generic.class);
   }

   @Override
   public Set<Ap.Module> getModulesAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, Ap.Module.class);
   }

   @Override
   public Set<Ap.Module> geModulesAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, Ap.Module.class);
   }

   @Override
   public Set<Ap.RecordComponent> getRecordComponentsAnnotatedWith(String qualifiedAnnotation)
   {
      return getAnnotated(qualifiedAnnotation, Ap.RecordComponent.class);
   }

   @Override
   public Set<Ap.RecordComponent> geRecordComponentsAnnotatedWith(C.Annotation annotation)
   {
      return getAnnotated(annotation, Ap.RecordComponent.class);
   }

   @Override
   public void writeAndCompileSourceFile(String qualifiedName, String content)
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
   public void writeResource(StandardLocation location, String moduleAndPkg, String relativePath, String content)
   {
      try (Writer writer = getProcessingEnv().getFiler().createResource(location, moduleAndPkg, relativePath).openWriter())
      {
         writer.write(content);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public FileObject readResource(StandardLocation location, String moduleAndPkg, String relativePath) throws IOException
   {
      return getProcessingEnv().getFiler().getResource(location, moduleAndPkg, relativePath);
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
   public void setExceptionHandler(BiConsumer<Ap.Context, Throwable> exceptionHandler)
   {
      this.exceptionHandler = exceptionHandler;
   }

   @Override
   public BiConsumer<Ap.Context, Throwable> getExceptionHandler()
   {
      return exceptionHandler;
   }

   @Override
   public void setDiagnosticHandler(BiConsumer<Ap.Context, DiagnosticContext> diagnosticHandler)
   {
      this.diagnosticHandler = diagnosticHandler;
   }

   @Override
   public BiConsumer<Ap.Context, DiagnosticContext> getDiagnosticHandler()
   {
      return diagnosticHandler;
   }

   @Override
   public void setSystemOutHandler(BiConsumer<Ap.Context, String> systemOutHandler)
   {
      this.systemOutHandler = systemOutHandler;
   }

   @Override
   public BiConsumer<Ap.Context, String> getSystemOutHandler()
   {
      return systemOutHandler;
   }

   @Override
   public void logAndRaiseError(String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
   }

   @Override
   public void logInfo(String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
   }

   @Override
   public void logWarning(String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg);
   }

   @Override
   public void logAndRaiseErrorAt(Ap.Annotationable annotationable, String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, adapt(annotationable).toElement());
   }

   @Override
   public void logInfoAt(Ap.Annotationable annotationable, String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg, adapt(annotationable).toElement());
   }

   @Override
   public void logWarningAt(Ap.Annotationable annotationable, String msg)
   {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg, adapt(annotationable).toElement());
   }

   @Override
   public Implementation getImplementation()
   {
      return implementation;
   }

   @Override
   public List<Ap.Module> getModules()
   {
      return elements.getAllModuleElements()
                     .stream()
                     .map(moduleElement -> adapt(this, moduleElement))
                     .toList();
   }

   @Override
   public Optional<Ap.Module> getModule(String name)
   {
      return ofNullable(elements.getModuleElement(name))
            .map(moduleElement -> adapt(this, moduleElement));
   }

   @Override
   public Ap.Module getModuleOrThrow(String name)
   {
      return getModule(name).orElseThrow();
   }

   @Override
   public List<Ap.Package> getPackage(String qualifiedName)
   {
      return elements.getAllPackageElements(qualifiedName)
                     .stream()
                     .map(packageElement -> adapt(this, packageElement))
                     .toList();
   }

   @Override
   public List<Ap.Package> getPackages()
   {
      return elements.getAllModuleElements()
                     .stream()
                     .flatMap(moduleElement -> moduleElement.getEnclosedElements().stream())
                     .map(PackageElement.class::cast)
                     .map(packageElement -> adapt(this, packageElement))
                     .toList();
   }

   @Override
   public Optional<Ap.Package> getPackage(String qualifiedModuleName, String qualifiedPackageName)
   {
      return getPackage(getModuleOrThrow(qualifiedModuleName), qualifiedPackageName);
   }

   @Override
   public Ap.Package getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName)
   {
      return getPackage(qualifiedModuleName, qualifiedPackageName).orElseThrow();
   }

   @Override
   public Optional<Ap.Package> getPackage(C.Module module, String qualifiedPackageName)
   {
      return ofNullable(elements.getPackageElement(adapt((Ap.Module) module).toModuleElement(), qualifiedPackageName))
            .map(packageElement -> adapt(this, packageElement));
   }

   @Override
   public Ap.Package getPackageOrThrow(C.Module module, String qualifiedPackageName)
   {
      return getPackage(module, qualifiedPackageName).orElseThrow();
   }

   @Override
   public Optional<Ap.Declared> getDeclared(String qualifiedName)
   {
      return ofNullable(elements.getTypeElement(qualifiedName))
            .map(typeElement -> adapt(this, typeElement));
   }

   @Override
   public List<Ap.Declared> getDeclared()
   {
      return getPackages()
            .stream()
            .flatMap(packageType -> requestOrThrow(packageType, PACKAGE_GET_DECLARED_LIST) .stream())
            .map(Ap.Declared.class::cast)
            .toList();
   }

   @Override
   public Constants getConstants()
   {
      return new ApConstantsImpl(this);
   }

   public static Set<Modifier> getModifiers(Element element)
   {
      Set<Modifier> result = element.getModifiers().stream().map(ApContextImpl::mapModifier).collect(Collectors.toSet());
      if ((element.getKind().isExecutable() || element.getKind().isDeclaredType() || element.getKind().isVariable()) &&
          !result.contains(Modifier.PUBLIC) &&
          !result.contains(Modifier.PROTECTED) &&
          !result.contains(Modifier.PRIVATE))
      {
         result.add(Modifier.PACKAGE_PRIVATE);
      }
      return Collections.unmodifiableSet(result);
   }

   private static Modifier mapModifier(javax.lang.model.element.Modifier modifier)
   {
      return switch (modifier)
      {
         case PUBLIC -> Modifier.PUBLIC;
         case PROTECTED -> Modifier.PROTECTED;
         case PRIVATE -> Modifier.PRIVATE;
         case ABSTRACT -> Modifier.ABSTRACT;
         case STATIC -> Modifier.STATIC;
         case SEALED -> Modifier.SEALED;
         case NON_SEALED -> Modifier.NON_SEALED;
         case FINAL -> Modifier.FINAL;
         case STRICTFP -> Modifier.STRICTFP;
         case DEFAULT -> Modifier.DEFAULT;
         case TRANSIENT -> Modifier.TRANSIENT;
         case VOLATILE -> Modifier.VOLATILE;
         case SYNCHRONIZED -> Modifier.SYNCHRONIZED;
         case NATIVE -> Modifier.NATIVE;
      };
   }

   public Types getTypes()
   {
      return types;
   }

   public Elements getElements()
   {
      return elements;
   }

   public Ap.Context getApi()
   {
      return this;
   }

   private ProcessingEnvironment getProcessingEnv()
   {
      return processingEnv;
   }

   private RoundEnvironment getRoundEnv()
   {
      return roundEnv;
   }
}
