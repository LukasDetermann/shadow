package io.determann.shadow.api.lang_model;

import io.determann.shadow.api.lang_model.shadow.structure.ModuleLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.PackageLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.RecordComponentLangModel;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Parameter;
import io.determann.shadow.api.shadow.structure.RecordComponent;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.*;

import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static java.util.Arrays.stream;

public interface LangModelContext
{
   List<DeclaredLangModel> getDeclared();

   Optional<DeclaredLangModel> getDeclared(String qualifiedName);

   default DeclaredLangModel getDeclaredOrThrow(String qualifiedName)
   {
      return getDeclared(qualifiedName).orElseThrow();
   }

   default List<AnnotationLangModel> getAnnotations()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ANNOTATION.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((AnnotationLangModel) declared))
                          .toList();
   }

   default Optional<AnnotationLangModel> getAnnotation(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(AnnotationLangModel.class::cast);
   }

   default AnnotationLangModel getAnnotationOrThrow(String qualifiedName)
   {
      return ((AnnotationLangModel) getDeclaredOrThrow(qualifiedName));
   }

   default List<ClassLangModel> getClasses()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.CLASS.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((ClassLangModel) declared))
                          .toList();
   }

   default Optional<ClassLangModel> getClass(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(ClassLangModel.class::cast);
   }

   default ClassLangModel getClassOrThrow(String qualifiedName)
   {
      return ((ClassLangModel) getDeclaredOrThrow(qualifiedName));
   }

   default List<EnumLangModel> getEnums()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ENUM.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((EnumLangModel) declared))
                          .toList();
   }

   default Optional<EnumLangModel> getEnum(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(EnumLangModel.class::cast);
   }

   default EnumLangModel getEnumOrThrow(String qualifiedName)
   {
      return ((EnumLangModel) getDeclaredOrThrow(qualifiedName));
   }

   default List<InterfaceLangModel> getInterfaces()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.INTERFACE.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((InterfaceLangModel) declared))
                          .toList();
   }

   default Optional<InterfaceLangModel> getInterface(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(InterfaceLangModel.class::cast);
   }

   default InterfaceLangModel getInterfaceOrThrow(String qualifiedName)
   {
      return ((InterfaceLangModel) getDeclaredOrThrow(qualifiedName));
   }

   default List<RecordLangModel> getRecords()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.RECORD.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((RecordLangModel) declared))
                          .toList();
   }

   default Optional<RecordLangModel> getRecord(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(RecordLangModel.class::cast);
   }

   default RecordLangModel getRecordOrThrow(String qualifiedName)
   {
      return ((RecordLangModel) getDeclaredOrThrow(qualifiedName));
   }

   List<ModuleLangModel> getModules();

   Optional<ModuleLangModel> getModule(String name);

   ModuleLangModel getModuleOrThrow(String name);

   /**
    * a package is unique per module. With multiple modules there can be multiple packages with the same name
    */
   List<PackageLangModel> getPackages(String qualifiedName);

   List<PackageLangModel> getPackages();

   Optional<PackageLangModel> getPackage(String qualifiedModuleName, String qualifiedPackageName);

   PackageLangModel getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName);

   Optional<PackageLangModel> getPackage(Module module, String qualifiedPackageName);

   PackageLangModel getPackageOrThrow(Module module, String qualifiedPackageName);

   LangModelConstants getConstants();

   /**
    * {@code shadowApi.getDeclaredOrThrow("java.util.List")} represents {@code List}
    * {@code shadowApi.getDeclaredOrThrow("java.util.List").withGenerics(shadowApi.getDeclaredOrThrow("java.lang.String"))} represents {@code List<String>}
    */
   ClassLangModel withGenerics(Class aClass, Shadow... generics);

   /**
    * like {@link #withGenerics(Class, Shadow...)} but resolves the names using {@link LangModelContext#getDeclaredOrThrow(String)}
    */
   default ClassLangModel withGenerics(Class aClass, String... qualifiedGenerics)
   {
      return withGenerics(aClass, stream(qualifiedGenerics)
            .map(this::getDeclaredOrThrow)
            .toArray(Shadow[]::new));
   }

   /**
    * {@code shadowApi.getDeclaredOrThrow("java.util.List")} represents {@code List}
    * {@code shadowApi.getDeclaredOrThrow("java.util.List").withGenerics(shadowApi.getDeclaredOrThrow("java.lang.String"))} represents {@code List<String>}
    */
   InterfaceLangModel withGenerics(Interface anInterface, Shadow... generics);

   /**
    * like {@link #withGenerics(Interface, Shadow...)} but resolves the names using {@link LangModelContext#getDeclaredOrThrow(String)}
    */
   default InterfaceLangModel withGenerics(Interface anInterface, String... qualifiedGenerics)
   {
      return withGenerics(anInterface, stream(qualifiedGenerics)
            .map(this::getDeclaredOrThrow)
            .toArray(Shadow[]::new));
   }

   /**
    * {@code shadowApi.getRecordOrThrow("org.example.MyRecord")} represents {@code MyRecord}
    * {@code shadowApi.getRecordOrThrow("org.example.MyRecord").withGenerics(shadowApi.getDeclaredOrThrow("java.lang.String"))} represents {@code MyRecord<String>}
    */
   RecordLangModel withGenerics(Record aRecord, Shadow... generics);

   /**
    * like {@link #withGenerics(Record, Shadow...)} but resolves the names using {@link LangModelContext#getDeclaredOrThrow(String)}
    */
   default RecordLangModel withGenerics(Record aRecord, String... qualifiedGenerics)
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
   ClassLangModel erasure(Class aClass);

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link Interface}s this means for example {@code interface MyInterface<T>{}} -&gt; {@code interface MyInterface{}}
    */
   InterfaceLangModel erasure(Interface anInterface);

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link Interface}s this means for example {@code interface MyInterface<T>{}} -&gt; {@code interface MyInterface{}}
    */
   RecordLangModel erasure(Record aRecord);

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link Array}s this means for example {@code T[]} -&gt; {@code java.lang.Object[]}
    */
   ArrayLangModel erasure(Array array);

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link Wildcard}s this means for example {@code ? extends java.lang.Number} -&gt; {@code java.lang.Number}
    */
   ShadowLangModel erasure(Wildcard wildcard);

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link Generic}s this means for example {@code T extends Number} -&gt; {@code Number}
    */
   ShadowLangModel erasure(Generic generic);

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
   ShadowLangModel erasure(Intersection intersection);

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link RecordComponent}s this means for example {@code record MyRecord<T>(T t){}} -> {@code record MyRecord<T>(java.lang.Object t){}}
    */
   RecordComponentLangModel erasure(RecordComponent recordComponent);

   /**
    * convince method returns the erasure of the parameter type
    *
    * @see LangModelContext#erasure(Class)  for example for more information on erasure
    */
   ShadowLangModel erasure(Parameter parameter);

   /**
    * convince method returns the erasure of the field type
    *
    * @see LangModelContext#erasure(Class)  for example for more information on erasure
    */
   ShadowLangModel erasure(Field field);

   /**
    * Used when constructing types to compare to at compile time that contain multiple, on each other depended, generics.
    * <p>
    * it answers the question: given {@snippet file="InterpolateGenericsExample.java" region="InterpolateGenerics.interpolateGenerics.code"}
    * and A being {@code String} what can B be by returning the "simplest" possible answer. in this case String
    * <p>
    * The code for the example
    * {@snippet file="InterpolateGenericsExample.java" region="InterpolateGenerics.interpolateGenerics"}
    */
   ClassLangModel interpolateGenerics(Class aClass);

   /**
    * Used when constructing types to compare to at compile time that contain multiple, on each other depended, generics.
    * <p>
    * it answers the question: given {@snippet file="InterpolateGenericsExample.java" region="InterpolateGenerics.interpolateGenerics.code"}
    * and A being {@code String} what can B be by returning the "simplest" possible answer. in this case String
    * <p>
    * The code for the example
    * {@snippet file="InterpolateGenericsExample.java" region="InterpolateGenerics.interpolateGenerics"}
    */
   InterfaceLangModel interpolateGenerics(Interface anInterface);

   /**
    * Used when constructing types to compare to at compile time that contain multiple, on each other depended, generics.
    * <p>
    * it answers the question: given {@snippet file="InterpolateGenericsExample.java" region="InterpolateGenerics.interpolateGenerics.code"}
    * and A being {@code String} what can B be by returning the "simplest" possible answer. in this case String
    * <p>
    * The code for the example
    * {@snippet file="InterpolateGenericsExample.java" region="InterpolateGenerics.interpolateGenerics"}
    */
   RecordLangModel interpolateGenerics(Record aRecord);

   /**
    * String[] -&gt; String[][]
    */
   ArrayLangModel asArray(Array array);

   /**
    * int -&gt; int[]
    */
   ArrayLangModel asArray(Primitive primitive);


   /**
    * String -&gt; String[]
    */
   ArrayLangModel asArray(Declared declared);

   /**
    * {@code Collection & Serializable} -&gt;  {@code Collection & Serializable[]}
    */
   ArrayLangModel asArray(Intersection intersection);

   WildcardLangModel asExtendsWildcard(Array array);

   WildcardLangModel asSuperWildcard(Array array);

   WildcardLangModel asExtendsWildcard(Declared array);

   WildcardLangModel asSuperWildcard(Declared array);
}
