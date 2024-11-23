package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.LM_Erasable;
import io.determann.shadow.api.lang_model.shadow.modifier.LM_FinalModifiable;
import io.determann.shadow.api.lang_model.shadow.modifier.LM_StaticModifiable;
import io.determann.shadow.api.lang_model.shadow.structure.LM_RecordComponent;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.api.shadow.type.C_Record;

import java.util.Collection;
import java.util.List;

import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;

public interface LM_Record extends C_Record,
                                   LM_Declared,
                                   LM_StaticModifiable,
                                   LM_FinalModifiable,
                                   LM_Erasable
{
   default LM_RecordComponent getRecordComponentOrThrow(String simpleName)
   {
      return getRecordComponents().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).findAny().orElseThrow();
   }

   List<LM_RecordComponent> getRecordComponents();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenericTypes"}
    */
   List<LM_Type> getGenericTypes();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
    */
   List<LM_Generic> getGenerics();

   /**
    * {@code context.getRecordOrThrow("org.example.MyRecord")} represents {@code MyRecord}
    * {@code context.getRecordOrThrow("org.example.MyRecord").withGenerics(context.getDeclaredOrThrow("java.lang.String"))} represents {@code MyRecord<String>}
    */
   LM_Record withGenerics(LM_Type... generics);

   /**
    * like {@link #withGenerics(LM_Type...)} but resolves the names using {@link LM_Context#getDeclaredOrThrow(String)}
    */
   LM_Record withGenerics(String... qualifiedGenerics);

   /**
    * Used when constructing types to compare to at compile time that contain multiple, on each other depended, generics.
    * <p>
    * it answers the question: given {@snippet file="InterpolateGenericsExample.java" region="InterpolateGenerics.interpolateGenerics.code"}
    * and A being {@code String} what can B be by returning the "simplest" possible answer. in this case String
    * <p>
    * The code for the example
    * {@snippet file="InterpolateGenericsExample.java" region="InterpolateGenerics.interpolateGenerics"}
    */
   LM_Record interpolateGenerics();

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link Collection}
    * {@code typeToTest.erasure().isSubtypeOf(context.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link C_Interface}s this means for example {@code interface MyInterface<T>{}} -&gt; {@code interface MyInterface{}}
    */
   @Override
   LM_Record erasure();
}
