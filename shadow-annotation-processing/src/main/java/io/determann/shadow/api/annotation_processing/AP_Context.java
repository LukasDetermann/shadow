package io.determann.shadow.api.annotation_processing;

import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.LM_Annotationable;
import io.determann.shadow.api.lang_model.shadow.structure.*;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.shadow.type.C_Annotation;
import io.determann.shadow.api.shadow.type.C_Type;

import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * This is the core class for a lightweight wrapper around the java annotationProcessor api. The {@link AP_Context} is transient. Meaning you can
 * transition between using the shadow and the java annotation processor api from line to line if you so wish.
 * <br><br>
 *
 * <h2>Usage:</h2>
 *
 * <ul>
 *    <li>get anything that is annotated {@link #getAnnotatedWith(String)}</li>
 *    <li>get already compiled sources {@link #getDeclaredOrThrow(String)} for example</li>
 *    <li>get constants {@link #getConstants()}</li>
 *    <li>log using {@link #logAndRaiseError(String)} or log at {@link #logAndRaiseErrorAt(LM_Annotationable, String)}</li>
 * </ul>
 *
 * @see AP_Processor
 * @see C_Type
 */
public interface AP_Context extends LM_Context
{
   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Annotationable> getAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Annotationable> getAnnotatedWith(C_Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Declared> getDeclaredAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Declared> getDeclaredAnnotatedWith(C_Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Class> getClassesAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Class> getClassesAnnotatedWith(C_Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Enum> getEnumsAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Enum> getEnumsAnnotatedWith(C_Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Interface> getInterfacesAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Interface> getInterfacesAnnotatedWith(C_Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Record> getRecordsAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Record> getRecordsAnnotatedWith(C_Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Field> getFieldsAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Field> getFieldsAnnotatedWith(C_Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Parameter> getParametersAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Parameter> getParametersAnnotatedWith(C_Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Method> getMethodsAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Method> getMethodsAnnotatedWith(C_Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Constructor> getConstructorsAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Constructor> getConstructorsAnnotatedWith(C_Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Annotation> getAnnotationsAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Annotation> getAnnotationsAnnotatedWith(C_Annotation annotation);
   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Package> getPackagesAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Package> gePackagesAnnotatedWith(C_Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Generic> getGenericsAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Generic> geGenericsAnnotatedWith(C_Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Module> getModulesAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_Module> geModulesAnnotatedWith(C_Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_RecordComponent> getRecordComponentsAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<LM_RecordComponent> geRecordComponentsAnnotatedWith(C_Annotation annotation);

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
   void setExceptionHandler(BiConsumer<AP_Context, Throwable> exceptionHandler);

   /**
    * @see #setExceptionHandler(BiConsumer)
    */
   BiConsumer<AP_Context, Throwable> getExceptionHandler();

   /**
    * Executed at the end of each round.
    * When the processing is over each Processor gets called one more time with {@link #isProcessingOver()} = true.
    */
   void setDiagnosticHandler(BiConsumer<AP_Context, AP_DiagnosticContext> diagnosticHandler);

   /**
    * @see #setDiagnosticHandler(BiConsumer)
    */
   BiConsumer<AP_Context, AP_DiagnosticContext> getDiagnosticHandler();

   /**
    * Some {@link javax.tools.Tool} don't support {@link System#out}. By default, it is proxied and redirected to the logger as warning
    */
   void setSystemOutHandler(BiConsumer<AP_Context, String> systemOutHandler);

   /**
    * @see #setSystemOutHandler(BiConsumer)
    */
   BiConsumer<AP_Context, String> getSystemOutHandler();

   /**
    * Some {@link javax.tools.Tool} don't support {@link System#err}. By default, it is proxied and redirected to the logger as error during
    */
   void setSystemErrorHandler(BiConsumer<AP_Context, String> systemErrorHandler);

   /**
    * @see #setSystemErrorHandler(BiConsumer)
    */
   BiConsumer<AP_Context, String> getSystemErrorHandler();

   void logAndRaiseError(String msg);

   void logInfo(String msg);

   void logWarning(String msg);

   void logAndRaiseErrorAt(LM_Annotationable annotationable, String msg);

   void logInfoAt(LM_Annotationable annotationable, String msg);

   void logWarningAt(LM_Annotationable annotationable, String msg);
}
