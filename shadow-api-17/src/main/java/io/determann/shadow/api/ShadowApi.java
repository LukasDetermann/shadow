package io.determann.shadow.api;

import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.metadata.JdkApi;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.impl.annotation_processing.ShadowApiImpl;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

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
 *    <li>convert between sourceCode representations. So called {@link Shadow}s {@link Converter#convert(Shadow)}</li>
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

   List<Module> getModules();

   Optional<Module> getModule(String name);

   Module getModuleOrThrow(String name);

   /**
    * a package is unique per module. With multiple modules there can be multiple packages with the same name
    */
   List<Package> getPackages(String qualifiedName);

   List<Package> getPackages();

   Optional<Package> getPackage(String qualifiedModuleName, String qualifiedPackageName);

   Package getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName);

   Optional<Package> getPackage(Module module, String qualifiedPackageName);

   Package getPackageOrThrow(Module module, String qualifiedPackageName);

   ShadowConstants getConstants();

   //writing files
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
    * @see Record#erasure()
    */
   static Declared erasure(Declared declared)
   {
      return ShadowApiImpl.erasure(declared);
   }
}
