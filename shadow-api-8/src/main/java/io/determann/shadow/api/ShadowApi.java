package io.determann.shadow.api;

import io.determann.shadow.api.converter.*;
import io.determann.shadow.api.metadata.JdkApi;
import io.determann.shadow.api.renderer.*;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.impl.ShadowApiImpl;
import io.determann.shadow.impl.converter.ConverterImpl;
import io.determann.shadow.impl.renderer.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * This is the core class for a lightweight wrapper around the java annotationProcessor api. The {@link ShadowApi} is transient. Meaning you can
 * transition between using the shadow and the java annotation processor api from line to line if you so wish. Methods in the ShadowApi that leak
 * the annotation processor api are annotated with {@link JdkApi}.
 * <br><br>
 *
 * <h2>Usage:</h2>
 *
 * <ul>
 *    <li>get anything that is annotated {@link #getAnnotatedWith(String)}</li>
 *    <li>get already compiled sources {@link #getDeclaredOrThrow(String)} for example</li>
 *    <li>get constants {@link #getConstants()}</li>
 *    <li>log using {@link #logError(String)} or log at {@link #logErrorAt(Annotationable, String)}</li>
 *    <li>convert between sourceCode representations. So called {@link Shadow}s {@link #convert(Shadow)}</li>
 * </ul>
 *
 * @see ShadowProcessor
 * @see JdkApi
 * @see Shadow
 */
public interface ShadowApi extends DeclaredHolder
{
   static ShadowApi create(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, int processingRound)
   {
      return new ShadowApiImpl(processingEnv, roundEnv, processingRound);
   }

   static ShadowApi create(ShadowApi apiFromPreviousRound, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, int processingRound)
   {
      if (apiFromPreviousRound == null)
      {
         return create(processingEnv, roundEnv, processingRound);
      }
      return ((ShadowApiImpl) apiFromPreviousRound).update(processingEnv, roundEnv, processingRound);
   }

   JdkApiContext getJdkApiContext();

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   AnnotationTypeChooser getAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   AnnotationTypeChooser getAnnotatedWith(Annotation annotation);

   Optional<Package> getPackage(String qualifiedName);

   Package getPackageOrThrow(String qualifiedName);

   ShadowConstants getConstants();

   //logging
   void logError(String msg);

   void logInfo(String msg);

   void logWarning(String msg);

   void logErrorAt(Annotationable elementBacked, String msg);

   void logInfoAt(Annotationable elementBacked, String msg);

   //writing files
   void logWarningAt(Annotationable elementBacked, String msg);

   /**
    * the created file will be registered for the next annotation processor round. writes .java files
    */
   void writeSourceFile(String qualifiedName, String content);

   /**
    * the created file will be registered for the next annotation processor round. writes .class files
    */
   void writeClassFile(String qualifiedName, String content);

   /**
    * the created file will NOT be registered for the next annotation processor round. writes anything
    */
   void writeResource(@JdkApi StandardLocation location, String moduleAndPkg, String relativPath, String content);

   @JdkApi
   FileObject readResource(@JdkApi StandardLocation location, String moduleAndPkg, String relativPath) throws IOException;

   /**
    * Last round of annotation processing
    */
   boolean isProcessingOver();

   /**
    * First round of annotation processing
    */
   boolean isFirstRound();

   /**
    * starts at 0
    */
   int getProcessingRound();

   /**
    * Consumer to handle exceptions that occur in this annotation processor.
    * If you want the compilation to stop because of it just throw any expedition.
    */
   void setExceptionHandler(BiConsumer<ShadowApi, Throwable> exceptionHandler);

   /**
    * @see #setExceptionHandler(BiConsumer)
    */
   BiConsumer<ShadowApi, Throwable> getExceptionHandler();

   /**
    * Executed at the end of each round.
    * When the processing is over each Processor gets called one more time with {@link #isProcessingOver()} = true.
    */
   void setDiagnosticHandler(BiConsumer<ShadowApi, DiagnosticContext> diagnosticHandler);

   /**
    * @see #setDiagnosticHandler(BiConsumer)
    */
   BiConsumer<ShadowApi, DiagnosticContext> getDiagnosticHandler();

   /**
    * Some {@link javax.tools.Tool} don't support {@link System#out}. By default, it is proxied and redirected to the logger as warning
    */
   void setSystemOutHandler(BiConsumer<ShadowApi, String> systemOutHandler);

   /**
    * @see #setSystemOutHandler(BiConsumer)
    */
   BiConsumer<ShadowApi, String> getSystemOutHandler();

   /**
    * Some {@link javax.tools.Tool} don't support {@link System#err}. By default, it is proxied and redirected to the logger as error during
    */
   void setSystemErrorHandler(BiConsumer<ShadowApi, String> systemErrorHandler);

   /**
    * @see #setSystemErrorHandler(BiConsumer)
    */
   BiConsumer<ShadowApi, String> getSystemErrorHandler();

   /**
    * Convince method that performs erasure on all declared types that support it
    *
    * @see Class#erasure()
    * @see Interface#erasure()
    */
   static Declared erasure(Declared declared)
   {
      return ShadowApiImpl.erasure(declared);
   }

   /**
    * all render methods produce qualified names
    * default is {@link #renderNamesWithoutNeedingImports()}
    *
    * @see #renderSimpleNames()
    * @see #renderNamesWithoutNeedingImports()
    * @see #onNameRendered(Consumer)
    */
   void renderQualifiedNames();

   /**
    * all render methods produce simple names.
    * default is {@link #renderNamesWithoutNeedingImports()}
    *
    * @see #renderQualifiedNames()
    * @see #renderNamesWithoutNeedingImports()
    * @see #onNameRendered(Consumer)
    */
   void renderSimpleNames();

   /**
    * all render methods produce qualified names except for the content of {@code java.lang}.
    * this is the default
    *
    * @see #renderSimpleNames()
    * @see #renderQualifiedNames()
    * @see #onNameRendered(Consumer)
    */
   void renderNamesWithoutNeedingImports();

   /**
    * @see #renderQualifiedNames()
    * @see #renderSimpleNames()
    * @see #renderNamesWithoutNeedingImports()
    */
   void onNameRendered(Consumer<NameRenderedEvent> onNameRendered);

   static AnnotationRenderer render(Annotation annotation)
   {
      return new AnnotationRendererImpl(annotation);
   }

   static AnnotationUsageRenderer render(AnnotationUsage annotationUsage)
   {
      return new AnnotationUsageRendererImpl(annotationUsage);
   }

   static ArrayRenderer render(Array array)
   {
      return new ArrayRendererImpl(array);
   }

   static ClassRenderer render(Class clazz)
   {
      return new ClassRendererImpl(clazz);
   }

   static ConstructorRenderer render(Constructor constructor)
   {
      return new ConstructorRendererImpl(constructor);
   }

   static EnumRenderer render(Enum enumType)
   {
      return new EnumRendererImpl(enumType);
   }

   static EnumConstantRenderer render(EnumConstant enumConstant)
   {
      return new EnumConstantRendererImpl(enumConstant);
   }

   static FieldRenderer render(Field field)
   {
      return new FieldRendererImpl(field);
   }

   static GenericRenderer render(Generic generic)
   {
      return new GenericRendererImpl(generic);
   }

   static InterfaceRenderer render(Interface interfaceType)
   {
      return new InterfaceRendererImpl(interfaceType);
   }

   static IntersectionRenderer render(Intersection intersection)
   {
      return new IntersectionRendererImpl(intersection);
   }

   static MethodRenderer render(Method method)
   {
      return new MethodRendererImpl(method);
   }

   static PackageRenderer render(Package packageType)
   {
      return new PackageRendererImpl(packageType);
   }

   static ParameterRenderer render(Parameter parameter)
   {
      return new ParameterRendererImpl(parameter);
   }

   static PrimitiveRenderer render(Primitive primitive)
   {
      return new PrimitiveRendererImpl(primitive);
   }

   static WildcardRenderer render(Wildcard wildcard)
   {
      return new WildcardRendererImpl(wildcard);
   }


   //convert Shadows
   static AnnotationConverter convert(Annotation annotationShadow)
   {
      return new ConverterImpl(annotationShadow);
   }

   static ArrayConverter convert(Array array)
   {
      return new ConverterImpl(array);
   }

   static ClassConverter convert(Class aClass)
   {
      return new ConverterImpl(aClass);
   }

   static ConstructorConverter convert(Constructor constructor)
   {
      return new ConverterImpl(constructor);
   }

   static DeclaredConverter convert(Declared declared)
   {
      return new ConverterImpl(declared);
   }

   static EnumConstantConverter convert(EnumConstant enumConstant)
   {
      return new ConverterImpl(enumConstant);
   }

   static EnumConverter convert(Enum enumShadow)
   {
      return new ConverterImpl(enumShadow);
   }

   static ExecutableConverter convert(Executable executable)
   {
      return new ConverterImpl(executable);
   }

   static FieldConverter convert(Field field)
   {
      return new ConverterImpl(field);
   }

   static InterfaceConverter convert(Interface interfaceShadow)
   {
      return new ConverterImpl(interfaceShadow);
   }

   static IntersectionConverter convert(Intersection intersection)
   {
      return new ConverterImpl(intersection);
   }

   static MethodConverter convert(Method methodShadow)
   {
      return new ConverterImpl(methodShadow);
   }

   static VoidConverter convert(Void aVoid)
   {
      return new ConverterImpl(aVoid);
   }

   static NullConverter convert(Null aNull)
   {
      return new ConverterImpl(aNull);
   }

   static PackageConverter convert(Package packageShadow)
   {
      return new ConverterImpl(packageShadow);
   }

   static ParameterConverter convert(Parameter parameter)
   {
      return new ConverterImpl(parameter);
   }

   static PrimitiveConverter convert(Primitive primitive)
   {
      return new ConverterImpl(primitive);
   }

   static ShadowConverter convert(Shadow shadow)
   {
      return new ConverterImpl(shadow);
   }

   static GenericConverter convert(Generic generic)
   {
      return new ConverterImpl(generic);
   }

   static VariableConverter convert(Variable<?> variable)
   {
      return new ConverterImpl(variable);
   }

   static WildcardConverter convert(Wildcard wildcard)
   {
      return new ConverterImpl(wildcard);
   }
}
