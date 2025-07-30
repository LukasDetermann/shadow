package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.LM_Erasable;
import io.determann.shadow.api.lang_model.shadow.modifier.LM_AbstractModifiable;
import io.determann.shadow.api.lang_model.shadow.modifier.LM_Sealable;
import io.determann.shadow.api.lang_model.shadow.modifier.LM_StaticModifiable;
import io.determann.shadow.api.shadow.type.C_Interface;

import java.util.List;

public non-sealed interface LM_Interface

      extends C_Interface,
              LM_Declared,
              LM_AbstractModifiable,
              LM_StaticModifiable,
              LM_Sealable,
              LM_Erasable
{
   boolean isFunctional();

   List<LM_Declared> getPermittedSubTypes();

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
   LM_Interface withGenerics(LM_Type... generics);

   /**
    * like {@link #withGenerics(LM_Type...)} but resolves the names using {@link LM_Context#getDeclaredOrThrow(String)}
    */
   LM_Interface withGenerics(String... qualifiedGenerics);

   /**
    * Used when constructing types to compare to at compile time that contain multiple, on each other depended, generics.
    * <p>
    * it answers the question: given {@snippet file = "InterpolateGenericsExample.java" region = "InterpolateGenerics.interpolateGenerics.code"}
    * and A being {@code String} what can B be by returning the "simplest" possible answer. in this case String
    * <p>
    * The code for the example
    * {@snippet file = "InterpolateGenericsExample.java" region = "InterpolateGenerics.interpolateGenerics"}
    */
   LM_Interface interpolateGenerics();

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code typeToTest.erasure().isSubtypeOf(context.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link C_Interface}s this means for example {@code interface MyInterface<T>{}} -&gt; {@code interface MyInterface{}}
    */
   @Override
   LM_Interface erasure();
}
