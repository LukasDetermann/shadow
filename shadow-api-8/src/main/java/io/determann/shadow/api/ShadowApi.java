package io.determann.shadow.api;

import io.determann.shadow.api.converter.*;
import io.determann.shadow.api.metadata.JdkApi;
import io.determann.shadow.api.metadata.Scope;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.impl.ShadowApiImpl;
import io.determann.shadow.impl.converter.ConverterImpl;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;

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
 * <li>{@link System#out} is proxied and redirected to the logger as warning during the compilation</li>
 * <li>{@link System#err} is proxied and redirected to the logger as error during the compilation</li>
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

   /**
    * a package is unique per module. With multiple modules there can be multiple packages with the same name
    */
   @Scope(ALL)
   Package getPackageOrThrow(String qualifiedName);

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
    * starts at 1
    */
   int getProcessingRound();

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
}
