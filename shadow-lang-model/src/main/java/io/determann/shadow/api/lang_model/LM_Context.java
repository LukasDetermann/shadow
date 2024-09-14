package io.determann.shadow.api.lang_model;

import io.determann.shadow.api.lang_model.shadow.structure.LM_Module;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Package;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.shadow.structure.C_Module;
import io.determann.shadow.api.shadow.type.*;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;

public interface LM_Context
{
   List<LM_Declared> getDeclared();

   Optional<LM_Declared> getDeclared(String qualifiedName);

   default LM_Declared getDeclaredOrThrow(String qualifiedName)
   {
      return getDeclared(qualifiedName).orElseThrow();
   }

   default List<LM_Annotation> getAnnotations()
   {
      return getDeclared().stream()
                          .filter(declared -> declared instanceof LM_Annotation)
                          .map(declared -> ((LM_Annotation) declared))
                          .toList();
   }

   default Optional<LM_Annotation> getAnnotation(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(LM_Annotation.class::cast);
   }

   default LM_Annotation getAnnotationOrThrow(String qualifiedName)
   {
      return ((LM_Annotation) getDeclaredOrThrow(qualifiedName));
   }

   default List<LM_Class> getClasses()
   {
      return getDeclared().stream()
                          .filter(declared -> declared instanceof LM_Class)
                          .map(declared -> ((LM_Class) declared))
                          .toList();
   }

   default Optional<LM_Class> getClass(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(LM_Class.class::cast);
   }

   default LM_Class getClassOrThrow(String qualifiedName)
   {
      return ((LM_Class) getDeclaredOrThrow(qualifiedName));
   }

   default List<LM_Enum> getEnums()
   {
      return getDeclared().stream()
                          .filter(declared -> declared instanceof C_Enum)
                          .map(declared -> ((LM_Enum) declared))
                          .toList();
   }

   default Optional<LM_Enum> getEnum(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(LM_Enum.class::cast);
   }

   default LM_Enum getEnumOrThrow(String qualifiedName)
   {
      return ((LM_Enum) getDeclaredOrThrow(qualifiedName));
   }

   default List<LM_Interface> getInterfaces()
   {
      return getDeclared().stream()
                          .filter(declared -> declared instanceof C_Interface)
                          .map(declared -> ((LM_Interface) declared))
                          .toList();
   }

   default Optional<LM_Interface> getInterface(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(LM_Interface.class::cast);
   }

   default LM_Interface getInterfaceOrThrow(String qualifiedName)
   {
      return ((LM_Interface) getDeclaredOrThrow(qualifiedName));
   }

   default List<LM_Record> getRecords()
   {
      return getDeclared().stream()
                          .filter(declared -> declared instanceof C_Record)
                          .map(declared -> ((LM_Record) declared))
                          .toList();
   }

   default Optional<LM_Record> getRecord(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(LM_Record.class::cast);
   }

   default LM_Record getRecordOrThrow(String qualifiedName)
   {
      return ((LM_Record) getDeclaredOrThrow(qualifiedName));
   }

   List<LM_Module> getModules();

   Optional<LM_Module> getModule(String name);

   LM_Module getModuleOrThrow(String name);

   /**
    * a package is unique per module. With multiple modules there can be multiple packages with the same name
    */
   List<LM_Package> getPackages(String qualifiedName);

   List<LM_Package> getPackages();

   Optional<LM_Package> getPackage(String qualifiedModuleName, String qualifiedPackageName);

   LM_Package getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName);

   Optional<LM_Package> getPackage(C_Module module, String qualifiedPackageName);

   LM_Package getPackageOrThrow(C_Module module, String qualifiedPackageName);

   LM_Constants getConstants();

   /**
    * {@code context.getDeclaredOrThrow("java.util.List")} represents {@code List}
    * {@code context.getDeclaredOrThrow("java.util.List").withGenerics(context.getDeclaredOrThrow("java.lang.String"))} represents {@code List<String>}
    */
   LM_Class withGenerics(C_Class aClass, C_Type... generics);

   /**
    * like {@link #withGenerics(C_Class, C_Type...)} but resolves the names using {@link LM_Context#getDeclaredOrThrow(String)}
    */
   default LM_Class withGenerics(C_Class aClass, String... qualifiedGenerics)
   {
      return withGenerics(aClass, stream(qualifiedGenerics)
            .map(this::getDeclaredOrThrow)
            .toArray(C_Type[]::new));
   }

   /**
    * {@code context.getDeclaredOrThrow("java.util.List")} represents {@code List}
    * {@code context.getDeclaredOrThrow("java.util.List").withGenerics(context.getDeclaredOrThrow("java.lang.String"))} represents {@code List<String>}
    */
   LM_Interface withGenerics(C_Interface anInterface, C_Type... generics);

   /**
    * like {@link #withGenerics(C_Interface, C_Type...)} but resolves the names using {@link LM_Context#getDeclaredOrThrow(String)}
    */
   default LM_Interface withGenerics(C_Interface anInterface, String... qualifiedGenerics)
   {
      return withGenerics(anInterface, stream(qualifiedGenerics)
            .map(this::getDeclaredOrThrow)
            .toArray(C_Type[]::new));
   }

   /**
    * {@code context.getRecordOrThrow("org.example.MyRecord")} represents {@code MyRecord}
    * {@code context.getRecordOrThrow("org.example.MyRecord").withGenerics(context.getDeclaredOrThrow("java.lang.String"))} represents {@code MyRecord<String>}
    */
   LM_Record withGenerics(C_Record aRecord, C_Type... generics);

   /**
    * like {@link #withGenerics(C_Record, C_Type...)} but resolves the names using {@link LM_Context#getDeclaredOrThrow(String)}
    */
   default LM_Record withGenerics(C_Record aRecord, String... qualifiedGenerics)
   {
      return withGenerics(aRecord, stream(qualifiedGenerics)
            .map(this::getDeclaredOrThrow)
            .toArray(C_Type[]::new));
   }

   /**
    * Used when constructing types to compare to at compile time that contain multiple, on each other depended, generics.
    * <p>
    * it answers the question: given {@snippet file="InterpolateGenericsExample.java" region="InterpolateGenerics.interpolateGenerics.code"}
    * and A being {@code String} what can B be by returning the "simplest" possible answer. in this case String
    * <p>
    * The code for the example
    * {@snippet file="InterpolateGenericsExample.java" region="InterpolateGenerics.interpolateGenerics"}
    */
   LM_Class interpolateGenerics(C_Class aClass);

   /**
    * Used when constructing types to compare to at compile time that contain multiple, on each other depended, generics.
    * <p>
    * it answers the question: given {@snippet file="InterpolateGenericsExample.java" region="InterpolateGenerics.interpolateGenerics.code"}
    * and A being {@code String} what can B be by returning the "simplest" possible answer. in this case String
    * <p>
    * The code for the example
    * {@snippet file="InterpolateGenericsExample.java" region="InterpolateGenerics.interpolateGenerics"}
    */
   LM_Interface interpolateGenerics(C_Interface anInterface);

   /**
    * Used when constructing types to compare to at compile time that contain multiple, on each other depended, generics.
    * <p>
    * it answers the question: given {@snippet file="InterpolateGenericsExample.java" region="InterpolateGenerics.interpolateGenerics.code"}
    * and A being {@code String} what can B be by returning the "simplest" possible answer. in this case String
    * <p>
    * The code for the example
    * {@snippet file="InterpolateGenericsExample.java" region="InterpolateGenerics.interpolateGenerics"}
    */
   LM_Record interpolateGenerics(C_Record aRecord);

   /**
    * String[] -&gt; String[][]
    */
   LM_Array asArray(C_Array array);

   /**
    * int -&gt; int[]
    */
   LM_Array asArray(C_Primitive primitive);


   /**
    * String -&gt; String[]
    */
   LM_Array asArray(C_Declared declared);

   /**
    * {@code Collection & Serializable} -&gt;  {@code Collection & Serializable[]}
    */
   LM_Array asArray(C_Intersection intersection);

   LM_Wildcard asExtendsWildcard(C_Array array);

   LM_Wildcard asSuperWildcard(C_Array array);

   LM_Wildcard asExtendsWildcard(C_Declared array);

   LM_Wildcard asSuperWildcard(C_Declared array);
}
