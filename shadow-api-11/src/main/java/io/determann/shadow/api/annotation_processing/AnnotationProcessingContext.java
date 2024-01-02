package io.determann.shadow.api.annotation_processing;

import io.determann.shadow.api.AnnotationTypeChooser;
import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.DeclaredHolder;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.metadata.JdkApi;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;

import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import static java.util.Arrays.stream;

/**
 * This is the core class for a lightweight wrapper around the java annotationProcessor api. The {@link AnnotationProcessingContext} is transient. Meaning you can
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
 *    <li>log using {@link #logAndRaiseError(String)} or log at {@link #logAndRaiseErrorAt(Annotationable, String)}</li>
 *    <li>convert between sourceCode representations. So called {@link Shadow}s {@link Converter#convert(Shadow)}</li>
 * </ul>
 *
 * @see ShadowProcessor
 * @see JdkApi
 * @see Shadow
 */
public interface AnnotationProcessingContext extends DeclaredHolder
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

   List<Module> getModules();

   Optional<Module> getModule(String name);

   Module getModuleOrThrow(String name);

   /**
    * a package is unique per module. With multiple modules there can be multiple packages with the same name
    */
   List<Package> getPackages(String qualifiedName);

   List<Package> getPackages();

   Package getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName);

   Optional<Package> getPackage(String qualifiedModuleName, String qualifiedPackageName);

   Package getPackageOrThrow(Module module, String qualifiedPackageName);

   Optional<Package> getPackage(Module module, String qualifiedPackageName);

   ShadowConstants getConstants();

   //writing files
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

   /**
    * {@code shadowApi.getDeclaredOrThrow("java.util.List")} represents {@code List}
    * {@code shadowApi.getDeclaredOrThrow("java.util.List").withGenerics(shadowApi.getDeclaredOrThrow("java.lang.String"))} represents {@code List<String>}
    */
   Class withGenerics(Class aClass, Shadow... generics);

   /**
    * like {@link #withGenerics(Class, Shadow...)} but resolves the names using {@link AnnotationProcessingContext#getDeclaredOrThrow(String)}
    */
   default Class withGenerics(Class aClass, String... qualifiedGenerics)
   {
      return withGenerics(aClass, stream(qualifiedGenerics)
            .map(this::getDeclaredOrThrow)
            .toArray(Shadow[]::new));
   }

   /**
    * {@code shadowApi.getDeclaredOrThrow("java.util.List")} represents {@code List}
    * {@code shadowApi.getDeclaredOrThrow("java.util.List").withGenerics(shadowApi.getDeclaredOrThrow("java.lang.String"))} represents {@code List<String>}
    */
   Interface withGenerics(Interface anInterface, Shadow... generics);

   /**
    * like {@link #withGenerics(Interface, Shadow...)} but resolves the names using {@link AnnotationProcessingContext#getDeclaredOrThrow(String)}
    */
   default Interface withGenerics(Interface anInterface, String... qualifiedGenerics)
   {
      return withGenerics(anInterface, stream(qualifiedGenerics)
            .map(this::getDeclaredOrThrow)
            .toArray(Shadow[]::new));
   }

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link Class}s this means for example {@code class MyClass<T>{}} -&gt; {@code class MyClass{}}
    */
   Declared erasure(Class aClass);

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link Interface}s this means for example {@code interface MyInterface<T>{}} -&gt; {@code interface MyInterface{}}
    */
   Declared erasure(Interface anInterface);

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link Array}s this means for example {@code T[]} -&gt; {@code java.lang.Object[]}
    */
   Array erasure(Array array);

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link Wildcard}s this means for example {@code ? extends java.lang.Number} -&gt; {@code java.lang.Number}
    */
   Shadow erasure(Wildcard wildcard);

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link Generic}s this means for example {@code T extends Number} -&gt; {@code Number}
    */
   Shadow erasure(Generic generic);

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <pre>{@code
    * The erasure of an IntersectionType is its first bound type
    * public class IntersectionExample<T extends Collection & Serializable>{} -> Collection
    * public class IntersectionExample<T extends Serializable & Collection>{} -> Serializable
    * }</pre>
    */
   Shadow erasure(Intersection intersection);

   /**
    * convince method returns the erasure of the parameter type
    *
    * @see AnnotationProcessingContext#erasure(Class)  for example for more information on erasure
    */
   Shadow erasure(Parameter parameter);

   /**
    * convince method returns the erasure of the field type
    *
    * @see AnnotationProcessingContext#erasure(Class)  for example for more information on erasure
    */
   Shadow erasure(Field field);

   /**
    * Used when constructing types to compare to at compile time that contain multiple, on each other depended, generics.
    * <p>
    * it answers the question: given {@code public class MyClass<A extends Comparable<B>, B extends Comparable<A>> {}} and A being {@code String}
    * what can B be by returning the "simplest" possible answer. in this case String
    * <p>
    * The code for the example
    * <pre>{@code
    *       Class declared = shadowApi.getClassOrThrow("io.determann.shadow.example.processed.MyClass")
    *                                 .withGenerics(shadowApi.getDeclaredOrThrow("java.lang.String"),
    *                                                  shadowApi.getConstants().getUnboundWildcard());
    *       Class capture = declared.interpolateGenerics();
    *
    *       Shadow<TypeMirror> stringRep = convert(capture.getGenerics().get(1))
    *                                                      .toGeneric()
    *                                                      .map(Generic::getExtends)
    *                                                      .map(shadowApi::convert)
    *                                                      .flatMap(ShadowConverter::toClassOrThrow)
    *                                                      .map(Class::getGenerics)
    *                                                      .map(shadows -> shadows.get(0))
    *                                                      .orElseThrow();
    *
    *       System.out.println(stringRep.representsSameType(shadowApi.getDeclaredOrThrow("java.lang.String")));
    * }</pre>
    * Note the use of the unboundWildcardConstant witch gets replaced by calling {@code capture()} with the result
    */
   Class interpolateGenerics(Class aClass);

   /**
    * Used when constructing types to compare to at compile time that contain multiple, on each other depended, generics.
    * <p>
    * it answers the question: given {@code public class MyClass<A extends Comparable<B>, B extends Comparable<A>> {}} and A being {@code String}
    * what can B be by returning the "simplest" possible answer. in this case String
    * <p>
    * The code for the example
    * <pre>{@code
    *       Class declared = shadowApi.getClassOrThrow("io.determann.shadow.example.processed.MyClass")
    *                                 .withGenerics(shadowApi.getDeclaredOrThrow("java.lang.String"),
    *                                                  shadowApi.getConstants().getUnboundWildcard());
    *       Class capture = declared.interpolateGenerics();
    *
    *       Shadow<TypeMirror> stringRep = convert(capture.getGenerics().get(1))
    *                                                      .toGeneric()
    *                                                      .map(Generic::getExtends)
    *                                                      .map(shadowApi::convert)
    *                                                      .flatMap(ShadowConverter::toClassOrThrow)
    *                                                      .map(Class::getGenerics)
    *                                                      .map(shadows -> shadows.get(0))
    *                                                      .orElseThrow();
    *
    *       System.out.println(stringRep.representsSameType(shadowApi.getDeclaredOrThrow("java.lang.String")));
    * }</pre>
    * Note the use of the unboundWildcardConstant witch gets replaced by calling {@code capture()} with the result
    */
   Interface interpolateGenerics(Interface anInterface);

   /**
    * String[] -&gt; String[][]
    */
   Array asArray(Array array);

   /**
    * int -&gt; int[]
    */
   Array asArray(Primitive primitive);


   /**
    * String -&gt; String[]
    */
   Array asArray(Declared declared);

   /**
    * {@code Collection & Serializable} -&gt;  {@code Collection & Serializable[]}
    */
   Array asArray(Intersection intersection);

   Wildcard asExtendsWildcard(Array array);

   Wildcard asSuperWildcard(Array array);

   Wildcard asExtendsWildcard(Declared array);

   Wildcard asSuperWildcard(Declared array);
}
