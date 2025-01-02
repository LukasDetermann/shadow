package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.LM_Erasable;
import io.determann.shadow.api.lang_model.shadow.modifier.LM_AbstractModifiable;
import io.determann.shadow.api.lang_model.shadow.modifier.LM_FinalModifiable;
import io.determann.shadow.api.lang_model.shadow.modifier.LM_Sealable;
import io.determann.shadow.api.lang_model.shadow.modifier.LM_StaticModifiable;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Constructor;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Property;
import io.determann.shadow.api.lang_model.shadow.type.primitive.LM_Primitive;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.List;
import java.util.Optional;

public interface LM_Class extends C_Class,
                                  LM_Declared,
                                  LM_AbstractModifiable,
                                  LM_StaticModifiable,
                                  LM_Sealable,
                                  LM_FinalModifiable,
                                  LM_Erasable
{
   /**
    * reruns the super class of this class. calling {@code getSuperClass())} on {@link Integer} will return {@link Number}.
    * For {@link Object} null will be returned
    */
   LM_Class getSuperClass();

   List<LM_Class> getPermittedSubClasses();

   List<LM_Property> getProperties();

   /**
    * Equivalent to {@link #isSubtypeOf(C_Type)} except for primitives.
    * if one is a primitive and the other is not it tries to convert them
    */
   boolean isAssignableFrom(C_Type type);

   List<LM_Constructor> getConstructors();

   /**
    * returns the outer type for not static classes
    */
   Optional<LM_Declared> getOuterType();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenericTypes"}
    */
   List<LM_Type> getGenericTypes();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
    */
   List<LM_Generic> getGenerics();

   /**
    * {@code context.getDeclaredOrThrow("java.util.List")} represents {@code List}
    * {@code context.getDeclaredOrThrow("java.util.List").withGenerics(context.getDeclaredOrThrow("java.lang.String"))} represents {@code List<String>}
    */
   LM_Class withGenerics(LM_Type... generics);

   /**
    * like {@link #withGenerics(LM_Type...)} but resolves the names using {@link LM_Context#getDeclaredOrThrow(String)}
    */
   LM_Class withGenerics(String... qualifiedGenerics);

   /**
    * Used when constructing types to compare to at compile time that contain multiple, on each other depended, generics.
    * <p>
    * it answers the question: given {@snippet file="InterpolateGenericsExample.java" region="InterpolateGenerics.interpolateGenerics.code"}
    * and A being {@code String} what can B be by returning the "simplest" possible answer. in this case String
    * <p>
    * The code for the example
    * {@snippet file="InterpolateGenericsExample.java" region="InterpolateGenerics.interpolateGenerics"}
    */
   LM_Class interpolateGenerics();

   /**
    * Integer -&gt; int<br>
    * Long -&gt; long<br>
    * etc...
    */
   LM_Primitive asUnboxed();

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code typeToTest.erasure().isSubtypeOf(context.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link C_Class}s this means for example {@code class MyClass<T>{}} -&gt; {@code class MyClass{}}
    */
   LM_Class erasure();
}
