package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.shadow.LM_Erasable;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.api.shadow.type.C_Wildcard;

import java.util.Optional;

/**
 * {@snippet id = "test":
 *  List<? extends Number>//@highlight substring="? extends Number"
 *}
 * or
 * {@snippet :
 *  List<? super Number>//@highlight substring="? super Number"
 *}
 */
public non-sealed interface LM_Wildcard

      extends C_Wildcard,
              LM_Type,
              LM_Erasable
{
   /**
    * {@snippet :
    *  List<? extends Number>//@highlight substring="? extends Number"
    *}
    */
   Optional<LM_Type> getExtends();

   /**
    * {@snippet :
    *  List<? super Number>//@highlight substring="? super Number"
    *}
    */
   Optional<LM_Type> getSuper();

   /**
    * {@snippet file = "WildcardTest.java" region = "Wildcard.contains"}
    */
   boolean contains(C_Type type);

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code typeToTest.erasure().isSubtypeOf(context.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link C_Wildcard}s this means for example {@code ? extends java.lang.Number} -&gt; {@code java.lang.Number}
    */
   @Override
   LM_Wildcard erasure();
}
