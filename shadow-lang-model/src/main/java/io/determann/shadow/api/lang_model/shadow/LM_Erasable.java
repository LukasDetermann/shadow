package io.determann.shadow.api.lang_model.shadow;

import io.determann.shadow.api.shadow.C_Erasable;
import io.determann.shadow.api.shadow.type.C_Class;

public interface LM_Erasable extends C_Erasable
{
   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code typeToTest.erasure().isSubtypeOf(context.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link C_Class}s this means for example {@code class MyClass<T>{}} -&gt; {@code class MyClass{}}
    */
   LM_Erasable erasure();
}
