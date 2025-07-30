package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.shadow.LM_Annotationable;
import io.determann.shadow.api.lang_model.shadow.LM_Erasable;
import io.determann.shadow.api.lang_model.shadow.LM_Nameable;
import io.determann.shadow.api.shadow.type.C_Generic;

import java.util.List;
import java.util.Optional;

/**
 * represents the generic parameter at a class, method, constructor etc.
 */
public non-sealed interface LM_Generic
      extends C_Generic,
              LM_Annotationable,
              LM_ReferenceType,
              LM_Nameable,
              LM_Erasable
{
   LM_Type getBound();

   /**
    * {@snippet :
    * T extends Collection & Serializable//@highlight substring="Collection & Serializable"
    *}
    */
   List<LM_Type> getBounds();

   List<LM_Interface> getAdditionalBounds();

   /// Can not be declared in java normal java code. some results of [LM_Class#interpolateGenerics()] can create a [C_Generic] with a super type
   Optional<LM_Type> getSuper();

   /**
    * returns the class, method constructor etc. this is the generic for
    */
   Object getEnclosing();

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code typeToTest.erasure().isSubtypeOf(context.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link C_Generic}s this means for example {@code T extends Number} -&gt; {@code Number}
    */
   @Override
   LM_Generic erasure();
}