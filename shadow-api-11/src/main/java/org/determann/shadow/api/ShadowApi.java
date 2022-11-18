package org.determann.shadow.api;

import org.determann.shadow.api.converter.*;
import org.determann.shadow.api.converter.module.*;
import org.determann.shadow.api.metadata.JdkApi;
import org.determann.shadow.api.metadata.QualifiedName;
import org.determann.shadow.api.metadata.Scope;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.Module;
import org.determann.shadow.api.shadow.Package;
import org.determann.shadow.api.shadow.Void;
import org.determann.shadow.api.shadow.*;
import org.determann.shadow.api.shadow.module.*;
import org.determann.shadow.impl.ShadowApiImpl;
import org.determann.shadow.impl.converter.ConverterImpl;
import org.determann.shadow.impl.converter.DirectiveConverterImpl;
import org.jetbrains.annotations.UnmodifiableView;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.List;

import static org.determann.shadow.api.metadata.Scope.ScopeType.ALL;
import static org.determann.shadow.api.metadata.Scope.ScopeType.CURRENT_COMPILATION;

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
public interface ShadowApi extends DeclaredHolder
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

   @Scope(ALL)
   @UnmodifiableView List<Module> getModules();

   @Scope(ALL)
   Module getModule(String name);

   /**
    * a package is unique per module. With multiple modules there can be multiple packages with the same name
    */
   @Scope(ALL)
   @UnmodifiableView List<Package> getPackagesOrThrow(@QualifiedName String qualifiedName);

   @Scope(ALL)
   @UnmodifiableView List<Package> getPackages();

   @Scope(ALL)
   Package getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName);

   @Scope(ALL)
   Package getPackageOrThrow(Module module, String qualifiedPackageName);

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
   void writeSourceFile(@QualifiedName String qualifiedName, String content);

   /**
    * the created file will be registered for the next annotation processor round. writes .class files
    */
   void writeClassFile(@QualifiedName String qualifiedName, String content);

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
