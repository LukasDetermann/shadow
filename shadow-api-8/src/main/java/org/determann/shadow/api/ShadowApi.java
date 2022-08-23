package org.determann.shadow.api;

import org.determann.shadow.api.converter.*;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.Package;
import org.determann.shadow.api.shadow.Void;
import org.determann.shadow.api.shadow.*;
import org.determann.shadow.impl.ShadowApiImpl;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;

import static org.determann.shadow.api.Scope.ScopeType.ALL;
import static org.determann.shadow.api.Scope.ScopeType.CURRENT_COMPILATION;

/**
 * This is the core class for a lightweight wrapper around the java annotationProcessor api. The {@link ShadowApi} is transient. Meaning you can
 * transition between using the shadow and the java annotation processor api from line to line if you so wish. Methods in the ShadowApi that leak
 * the annotation processor api are annotated with {@link JdkApi}.
 * <br><br>
 *
 * <h2>Usage:</h2>
 *
 * <ul>
 *    <li>get anything that is annotated {@link #annotatedWith(String)}</li>
 *    <li>get already compiled sources {@link Scope.ScopeType#ALL}</li>
 *    <li>get constants {@link #getConstants()}</li>
 *    <li>log using {@link #logError(String)} or log at {@link #logErrorAt(ElementBacked, String)}</li>
 *    <li>convert between sourceCode representations. So called {@link Shadow}s {@link #convert(Shadow)}</li>
 * </ul>
 *
 * <h2>fyi:</h2>
 * <li>{@link System#out} is proxied and redirected to the logger as warning during the compilation</li>
 * <li>{@link System#err} is proxied and redirected to the logger as error during the compilation</li>
 *
 * @see ShadowProcessor
 * @see JdkApi
 * @see Shadow
 * @see Scope
 */
public interface ShadowApi
{
   static ShadowApi create(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, int processingRoundNumber)
   {
      return new ShadowApiImpl(processingEnv, roundEnv, processingRoundNumber);
   }

   JdkApiContext getJdkApiContext();

   /**
    * @see Scope.ScopeType#CURRENT_COMPILATION
    * @see #writeSourceFile(String, String)
    * @see #writeClassFile(String, String)
    */
   @Scope(CURRENT_COMPILATION)
   AnnotationTypeChooser annotatedWith(String qualifiedAnnotation);

   /**
    * a package is unique per module. With multiple modules there can be multiple packages with the same name
    */
   @Scope(ALL)
   Package getPackage(String qualifiedName);

   @Scope(ALL)
   Declared getDeclared(String qualifiedName);

   @Scope(ALL)
   Annotation getAnnotation(String qualifiedName);

   @Scope(ALL)
   Class getClass(String qualifiedName);

   @Scope(ALL)
   Enum getEnum(String qualifiedName);

   @Scope(ALL)
   Interface getInterface(String qualifiedName);

   ShadowConstants getConstants();

   ShadowFactory getShadowFactory();

   //String conversion

   /**
    * ExampleName -> ExampleName
    * org.example.ExampleName -> ExampleName
    * exampleName -> ExampleName
    * org.example.exampleName -> ExampleName
    * EXAMPLE_NAME -> ExampleName
    * org.example.EXAMPLE_NAME -> ExampleName
    */
   String to_UpperCamelCase(String toModify);

   /**
    * ExampleName -> exampleName
    * org.example.ExampleName -> exampleName
    * exampleName -> exampleName
    * org.example.exampleName -> exampleName
    * EXAMPLE_NAME -> exampleName
    * org.example.EXAMPLE_NAME -> exampleName
    */
   String to_lowerCamelCase(String toModify);

   /**
    * ExampleName -> EXAMPLE_NAME
    * org.example.ExampleName -> EXAMPLE_NAME
    * exampleName -> exampleName
    * org.example.exampleName -> EXAMPLE_NAME
    * EXAMPLE_NAME -> exampleName
    * org.example.EXAMPLE_NAME -> EXAMPLE_NAME
    */
   String to_SCREAMING_SNAKE_CASE(String toModify);

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
   FileObject readOResource(@JdkApi StandardLocation location, String moduleAndPkg, String relativPath) throws IOException;

   /**
    * Last round of annotation processing
    */
   boolean isLastRound();

   /**
    * First round of annotation processing
    */
   boolean isFirstRound();

   /**
    * Number of processing round
    */
   int roundNumber();

   //convert Shadows
   AnnotationConverter convert(Annotation annotationShadow);

   ArrayConverter convert(Array array);

   ClassConverter convert(Class aClass);

   ConstructorConverter convert(Constructor constructor);

   DeclaredConverter convert(Declared declared);

   EnumConstantConverter convert(EnumConstant enumConstant);

   EnumConverter convert(Enum enumShadow);

   ExecutableConverter convert(Executable executable);

   FieldConverter convert(Field field);

   InterfaceConverter convert(Interface interfaceShadow);

   IntersectionConverter convert(Intersection intersection);

   MethodConverter convert(Method methodShadow);

   VoidConverter convert(Void aVoid);

   NullConverter convert(Null aNull);

   PackageConverter convert(Package packageShadow);

   ParameterConverter convert(Parameter parameter);

   PrimitiveConverter convert(Primitive primitive);

   ShadowConverter convert(Shadow<? extends TypeMirror> shadow);

   GenericConverter convert(Generic generic);

   VariableConverter convert(Variable<?> variable);

   WildcardConverter convert(Wildcard wildcard);
}
