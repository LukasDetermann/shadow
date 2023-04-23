package io.determann.shadow.api;

import io.determann.shadow.api.converter.*;
import io.determann.shadow.api.converter.module.*;
import io.determann.shadow.api.metadata.JdkApi;
import io.determann.shadow.api.metadata.Scope;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.api.shadow.module.*;
import io.determann.shadow.impl.ShadowApiImpl;
import io.determann.shadow.impl.converter.ConverterImpl;
import io.determann.shadow.impl.converter.DirectiveConverterImpl;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;

import static io.determann.shadow.api.metadata.Scope.ScopeType.ALL;
import static io.determann.shadow.api.metadata.Scope.ScopeType.CURRENT_COMPILATION;

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
 *    <li>get already compiled sources {@link Scope.ScopeType#ALL}</li>
 *    <li>get constants {@link #getConstants()}</li>
 *    <li>log using {@link #logError(String)} or log at {@link #logErrorAt(ElementBacked, String)}</li>
 *    <li>convert between sourceCode representations. So called {@link Shadow}s {@link #convert(Shadow)}</li>
 * </ul>
 *
 * <h2>fyi:</h2>
 * <li>{@link System#out} is proxied and redirected to the logger as warning</li>
 * <li>{@link System#err} is proxied and redirected to the logger as error</li>
 *
 * @see ShadowProcessor
 * @see JdkApi
 * @see Shadow
 * @see Scope
 */
public interface ShadowApi extends DeclaredHolder
{
   static ShadowApi create(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, int processingRound)
   {
      return new ShadowApiImpl(processingEnv, roundEnv, processingRound);
   }

   JdkApiContext getJdkApiContext();

   /**
    * @see Scope.ScopeType#CURRENT_COMPILATION
    * @see #writeSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   @Scope(CURRENT_COMPILATION)
   AnnotationTypeChooser getAnnotatedWith(String qualifiedAnnotation);

   @Scope(CURRENT_COMPILATION)
   AnnotationTypeChooser getAnnotatedWith(Annotation annotation);

   @Scope(ALL)
   List<Module> getModules();

   @Scope(ALL)
   Module getModuleOrThrow(String name);

   /**
    * a package is unique per module. With multiple modules there can be multiple packages with the same name
    */
   @Scope(ALL)
   List<Package> getPackagesOrThrow(String qualifiedName);

   @Scope(ALL)
   List<Package> getPackages();

   @Scope(ALL)
   Package getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName);

   @Scope(ALL)
   Package getPackageOrThrow(Module module, String qualifiedPackageName);

   ShadowConstants getConstants();

   ShadowFactory getShadowFactory();

   //logging
   void logError(String msg);

   void logInfo(String msg);

   void logWarning(String msg);

   void logErrorAt(ElementBacked<?> elementBacked, String msg);

   void logInfoAt(ElementBacked<?> elementBacked, String msg);

   //writing files
   void logWarningAt(ElementBacked<?> elementBacked, String msg);

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
    * starts at 1
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
    * @see Record#erasure()
    */
   static Declared erasure(Declared declared)
   {
      return ShadowApiImpl.erasure(declared);
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

   static ModuleConverter convert(Module module)
   {
      return new ConverterImpl(module);
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

   static RecordComponentConverter convert(RecordComponent recordComponent)
   {
      return new ConverterImpl(recordComponent);
   }

   static RecordConverter convert(Record recordShadow)
   {
      return new ConverterImpl(recordShadow);
   }

   static ShadowConverter convert(Shadow<? extends TypeMirror> shadow)
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

   static DirectiveConverter convert(Directive directive)
   {
      return new DirectiveConverterImpl(directive);
   }

   static ExportsConverter convert(Exports exportsShadow)
   {
      return new DirectiveConverterImpl(exportsShadow);
   }

   static OpensConverter convert(Opens opensShadow)
   {
      return new DirectiveConverterImpl(opensShadow);
   }

   static ProvidesConverter convert(Provides providesShadow)
   {
      return new DirectiveConverterImpl(providesShadow);
   }

   static RequiresConverter convert(Requires requiresShadow)
   {
      return new DirectiveConverterImpl(requiresShadow);
   }

   static UsesConverter convert(Uses usesShadow)
   {
      return new DirectiveConverterImpl(usesShadow);
   }
}
