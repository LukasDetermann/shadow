package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.shadow.LM_Erasable;
import io.determann.shadow.api.shadow.type.C_Intersection;

import java.util.List;

/**
 * {@snippet :
 * T extends Collection & Serializable//@highlight substring="Collection & Serializable"
 *}
 */
public non-sealed interface LM_Intersection

      extends LM_Type,
              C_Intersection,
              LM_Erasable
{
   /**
    * {@snippet :
    * T extends Collection & Serializable//@highlight substring="Collection & Serializable"
    *}
    */
   List<LM_Type> getBounds();

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code typeToTest.erasure().isSubtypeOf(context.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <pre>{@code
    * The erasure of an IntersectionType is its first bound type
    * public class IntersectionExample<T extends Collection & Serializable>{} -> Collection
    * public class IntersectionExample<T extends Serializable & Collection>{} -> Serializable
    * }</pre>
    */
   @Override
   LM_Intersection erasure();

   /**
    * {@code Collection & Serializable} -&gt;  {@code Collection & Serializable[]}
    */
   LM_Array asArray();
}
