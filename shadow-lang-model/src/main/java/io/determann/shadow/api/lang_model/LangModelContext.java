package io.determann.shadow.api.lang_model;

import io.determann.shadow.api.DeclaredHolder;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;

public interface LangModelContext extends DeclaredHolder
{
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

   LangModelConstants getConstants();

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
    * {@code shadowApi.getRecordOrThrow("org.example.MyRecord")} represents {@code MyRecord}
    * {@code shadowApi.getRecordOrThrow("org.example.MyRecord").withGenerics(shadowApi.getDeclaredOrThrow("java.lang.String"))} represents {@code MyRecord<String>}
    */
   Record withGenerics(Record aRecord, Shadow... generics);

   /**
    * like {@link #withGenerics(Record, Shadow...)} but resolves the names using {@link AnnotationProcessingContext#getDeclaredOrThrow(String)}
    */
   default Record withGenerics(Record aRecord, String... qualifiedGenerics)
   {
      return withGenerics(aRecord, stream(qualifiedGenerics)
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
    * for {@link Interface}s this means for example {@code interface MyInterface<T>{}} -&gt; {@code interface MyInterface{}}
    */
   Declared erasure(Record aRecord);

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
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link RecordComponent}s this means for example {@code record MyRecord<T>(T t){}} -> {@code record MyRecord<T>(java.lang.Object t){}}
    */
   RecordComponent erasure(RecordComponent recordComponent);

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
   Record interpolateGenerics(Record aRecord);

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