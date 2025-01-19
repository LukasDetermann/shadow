package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.shadow.LM_Erasable;
import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Intersection;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.List;

public non-sealed interface LM_Array

      extends C_Array,
              LM_Erasable,
              LM_Type
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a type implements for example a
    * {@link java.util.Collection} {@snippet file = "GenericUsageTest.java" region = "GenericUsage.isSubtypeOf"}
    */
   boolean isSubtypeOf(C_Type type);

   /**
    * {@snippet :
    *  String[]//@highlight substring="String"
    *}
    */
   LM_Type getComponentType();

   /**
    * returns Object[] for declared Arrays and an {@link C_Intersection} of {@code java.io.Serializable&java.lang.Cloneable}
    * for primitive Arrays
    */
   List<LM_Type> getDirectSuperTypes();

   /**
    * String[] -&gt; String[][]
    */
   LM_Array asArray();


   LM_Wildcard asExtendsWildcard();

   LM_Wildcard asSuperWildcard();

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code typeToTest.erasure().isSubtypeOf(context.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link C_Array}s this means for example {@code T[]} -&gt; {@code java.lang.Object[]}
    */
   @Override
   LM_Array erasure();
}
