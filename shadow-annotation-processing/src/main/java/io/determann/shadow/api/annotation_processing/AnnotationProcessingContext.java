package io.determann.shadow.api.annotation_processing;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Annotation;
import io.determann.shadow.api.shadow.Shadow;

import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * This is the core class for a lightweight wrapper around the java annotationProcessor api. The {@link AnnotationProcessingContext} is transient. Meaning you can
 * transition between using the shadow and the java annotation processor api from line to line if you so wish.
 * <br><br>
 *
 * <h2>Usage:</h2>
 *
 * <ul>
 *    <li>get anything that is annotated {@link #getAnnotatedWith(String)}</li>
 *    <li>get already compiled sources {@link #getDeclaredOrThrow(String)} for example</li>
 *    <li>get constants {@link #getConstants()}</li>
 *    <li>log using {@link #logAndRaiseError(String)} or log at {@link #logAndRaiseErrorAt(Annotationable, String)}</li>
 *    <li>convert between sourceCode representations. So called {@link Shadow}s {@link Converter#convert(Shadow)}</li>
 * </ul>
 *
 * @see ShadowProcessor
 * @see Shadow
 */
public interface AnnotationProcessingContext extends LangModelContext
{
   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   AnnotationTypeChooser getAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   AnnotationTypeChooser getAnnotatedWith(Annotation annotation);

   /**
    * the created file will be registered for the next annotation processor round. writes .java files
    */
   void writeAndCompileSourceFile(String qualifiedName, String content);

   /**
    * the created file will be registered for the next annotation processor round. writes .class files
    */
   void writeClassFile(String qualifiedName, String content);

   /**
    * the created file will NOT be registered for the next annotation processor round. writes anything
    */
   void writeResource(StandardLocation location, String moduleAndPkg, String relativPath, String content);

   FileObject readResource(StandardLocation location, String moduleAndPkg, String relativPath) throws IOException;

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
   void setExceptionHandler(BiConsumer<AnnotationProcessingContext, Throwable> exceptionHandler);

   /**
    * @see #setExceptionHandler(BiConsumer)
    */
   BiConsumer<AnnotationProcessingContext, Throwable> getExceptionHandler();

   /**
    * Executed at the end of each round.
    * When the processing is over each Processor gets called one more time with {@link #isProcessingOver()} = true.
    */
   void setDiagnosticHandler(BiConsumer<AnnotationProcessingContext, DiagnosticContext> diagnosticHandler);

   /**
    * @see #setDiagnosticHandler(BiConsumer)
    */
   BiConsumer<AnnotationProcessingContext, DiagnosticContext> getDiagnosticHandler();

   /**
    * Some {@link javax.tools.Tool} don't support {@link System#out}. By default, it is proxied and redirected to the logger as warning
    */
   void setSystemOutHandler(BiConsumer<AnnotationProcessingContext, String> systemOutHandler);

   /**
    * @see #setSystemOutHandler(BiConsumer)
    */
   BiConsumer<AnnotationProcessingContext, String> getSystemOutHandler();

   /**
    * Some {@link javax.tools.Tool} don't support {@link System#err}. By default, it is proxied and redirected to the logger as error during
    */
   void setSystemErrorHandler(BiConsumer<AnnotationProcessingContext, String> systemErrorHandler);

   /**
    * @see #setSystemErrorHandler(BiConsumer)
    */
   BiConsumer<AnnotationProcessingContext, String> getSystemErrorHandler();

   void logAndRaiseError(String msg);

   void logInfo(String msg);

   void logWarning(String msg);

   void logAndRaiseErrorAt(Annotationable annotationable, String msg);

   void logInfoAt(Annotationable annotationable, String msg);

   void logWarningAt(Annotationable annotationable, String msg);
}
