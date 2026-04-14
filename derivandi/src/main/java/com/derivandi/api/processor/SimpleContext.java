package com.derivandi.api.processor;

import com.derivandi.api.Ap;
import com.derivandi.api.dsl.declared.DeclaredRenderable;
import com.derivandi.api.dsl.module.ModuleRenderable;
import com.derivandi.api.dsl.package_.PackageRenderable;

import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.derivandi.api.dsl.RenderingContext.createRenderingContext;

/**
 * This is the core class for a lightweight wrapper around the java annotationProcessor api. The {@link SimpleContext} is transient. Meaning you can
 * transition between using the shadow and the java annotation processor api from line to line if you so wish.
 * <br><br>
 *
 * <h2>Usage:</h2>
 *
 * <ul>
 *    <li>get anything that is annotated {@link #getAnnotatedWith(String)}</li>
 *    <li>get already compiled sources {@link #getDeclaredOrThrow(String)} for example</li>
 *    <li>get constants {@link #getConstants()}</li>
 *    <li>log using {@link #logAndRaiseError(String)} or log at {@link #logAndRaiseErrorAt(Ap.Annotationable, String)}</li>
 * </ul>
 *
 * @see Processor
 * @see Ap.Type
 */
public interface SimpleContext
{
   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Annotationable> getAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Annotationable> getAnnotatedWith(Ap.Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Declared> getDeclaredAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Declared> getDeclaredAnnotatedWith(Ap.Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Class> getClassesAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Class> getClassesAnnotatedWith(Ap.Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Enum> getEnumsAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Enum> getEnumsAnnotatedWith(Ap.Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Interface> getInterfacesAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Interface> getInterfacesAnnotatedWith(Ap.Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Record> getRecordsAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Record> getRecordsAnnotatedWith(Ap.Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Field> getFieldsAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Field> getFieldsAnnotatedWith(Ap.Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Parameter> getParametersAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Parameter> getParametersAnnotatedWith(Ap.Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Method> getMethodsAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Method> getMethodsAnnotatedWith(Ap.Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Constructor> getConstructorsAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Constructor> getConstructorsAnnotatedWith(Ap.Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Annotation> getAnnotationsAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Annotation> getAnnotationsAnnotatedWith(Ap.Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Package> getPackagesAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Package> gePackagesAnnotatedWith(Ap.Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Generic> getGenericDeclarationsAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Generic> geGenericsAnnotatedWith(Ap.Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Module> getModulesAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.Module> geModulesAnnotatedWith(Ap.Annotation annotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.RecordComponent> getRecordComponentsAnnotatedWith(String qualifiedAnnotation);

   /**
    * Looks up annotated elements in currently compiled code. <br>
    * Annotation processing happens in rounds.
    * If a processor generates sources the next round will contain only these.
    *
    * @see #writeAndCompileSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   Set<Ap.RecordComponent> geRecordComponentsAnnotatedWith(Ap.Annotation annotation);

   /**
    * the created file will be registered for the next annotation processor round. writes .java files
    */
   void writeAndCompileSourceFile(String qualifiedName, String content);

   /**
    * the created file will be registered for the next annotation processor round. writes .java files
    */
   default void writeAndCompileSourceFile(DeclaredRenderable declaredRenderable)
   {
      writeAndCompileSourceFile(declaredRenderable.renderQualifiedName(createRenderingContext()),
                                declaredRenderable.renderDeclaration(createRenderingContext()));
   }

   /**
    * the created file will be registered for the next annotation processor round. writes .java files
    */
   default void writeAndCompileSourceFile(ModuleRenderable moduleRenderable)
   {
      writeAndCompileSourceFile(moduleRenderable.renderQualifiedName(createRenderingContext()),
                                moduleRenderable.renderModuleInfo(createRenderingContext()));
   }

   /**
    * the created file will be registered for the next annotation processor round. writes .java files
    */
   default void writeAndCompileSourceFile(PackageRenderable packageRenderable)
   {
      writeAndCompileSourceFile(packageRenderable.renderQualifiedName(createRenderingContext()),
                                packageRenderable.renderPackageInfo(createRenderingContext()));
   }

   /**
    * the created file will be registered for the next annotation processor round. writes .class files
    */
   void writeClassFile(String qualifiedName, String content);

   /**
    * the created file will NOT be registered for the next annotation processor round. writes anything
    */
   void writeResource(StandardLocation location, String moduleAndPkg, String relativePath, String content);

   FileObject readResource(StandardLocation location, String moduleAndPkg, String relativePath) throws IOException;

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

   void logAndRaiseError(String msg);

   void logInfo(String msg);

   void logWarning(String msg);

   void logAndRaiseErrorAt(Ap.Annotationable annotationable, String msg);

   void logInfoAt(Ap.Annotationable annotationable, String msg);

   void logWarningAt(Ap.Annotationable annotationable, String msg);

   List<Ap.Declared> getDeclared();

   Optional<Ap.Declared> getDeclared(String qualifiedName);

   default Ap.Declared getDeclaredOrThrow(String qualifiedName)
   {
      return getDeclared(qualifiedName).orElseThrow();
   }

   default List<Ap.Annotation> getAnnotations()
   {
      return getDeclared().stream()
                          .filter(Ap.Annotation.class::isInstance)
                          .map(Ap.Annotation.class::cast)
                          .toList();
   }

   default Optional<Ap.Annotation> getAnnotation(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(Ap.Annotation.class::cast);
   }

   default Ap.Annotation getAnnotationOrThrow(String qualifiedName)
   {
      return ((Ap.Annotation) getDeclaredOrThrow(qualifiedName));
   }

   default List<Ap.Class> getClasses()
   {
      return getDeclared().stream()
                          .filter(Ap.Class.class::isInstance)
                          .map(Ap.Class.class::cast)
                          .toList();
   }

   default Optional<Ap.Class> getClass(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(Ap.Class.class::cast);
   }

   default Ap.Class getClassOrThrow(String qualifiedName)
   {
      return ((Ap.Class) getDeclaredOrThrow(qualifiedName));
   }

   default List<Ap.Enum> getEnums()
   {
      return getDeclared().stream()
                          .filter(Ap.Enum.class::isInstance)
                          .map(Ap.Enum.class::cast)
                          .toList();
   }

   default Optional<Ap.Enum> getEnum(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(Ap.Enum.class::cast);
   }

   default Ap.Enum getEnumOrThrow(String qualifiedName)
   {
      return ((Ap.Enum) getDeclaredOrThrow(qualifiedName));
   }

   default List<Ap.Interface> getInterfaces()
   {
      return getDeclared().stream()
                          .filter(Ap.Interface.class::isInstance)
                          .map(Ap.Interface.class::cast)
                          .toList();
   }

   default Optional<Ap.Interface> getInterface(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(Ap.Interface.class::cast);
   }

   default Ap.Interface getInterfaceOrThrow(String qualifiedName)
   {
      return ((Ap.Interface) getDeclaredOrThrow(qualifiedName));
   }

   default List<Ap.Record> getRecords()
   {
      return getDeclared().stream()
                          .filter(Ap.Record.class::isInstance)
                          .map(Ap.Record.class::cast)
                          .toList();
   }

   default Optional<Ap.Record> getRecord(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(Ap.Record.class::cast);
   }

   default Ap.Record getRecordOrThrow(String qualifiedName)
   {
      return ((Ap.Record) getDeclaredOrThrow(qualifiedName));
   }

   List<Ap.Module> getModules();

   Optional<Ap.Module> getModule(String name);

   Ap.Module getModuleOrThrow(String name);

   /**
    * a package is unique per module. With multiple modules there can be multiple packages with the same name
    */
   List<Ap.Package> getPackage(String qualifiedName);

   List<Ap.Package> getPackages();

   Optional<Ap.Package> getPackage(String qualifiedModuleName, String qualifiedPackageName);

   Ap.Package getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName);

   Optional<Ap.Package> getPackage(Ap.Module module, String qualifiedPackageName);

   Ap.Package getPackageOrThrow(Ap.Module module, String qualifiedPackageName);

   Constants getConstants();
}
