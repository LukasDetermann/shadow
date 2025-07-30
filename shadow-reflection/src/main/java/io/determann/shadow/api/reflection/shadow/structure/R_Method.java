package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.modifier.*;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.structure.C_Method;

import java.lang.annotation.ElementType;

public non-sealed interface R_Method

      extends C_Method,
              R_Executable,
              R_StaticModifiable,
              R_DefaultModifiable,
              R_AccessModifiable,
              R_AbstractModifiable,
              R_FinalModifiable,
              R_StrictfpModifiable,
              R_NativeModifiable
{
   R_Type getReturnType();

   boolean overrides(C_Method method);

   boolean overwrittenBy(C_Method method);

   /**
    * <pre>{@code
    * Do both methods have the same parameter types in the same order?
    * a() && b() -> true
    * a(String name) && b() -> false
    * a(String name, Long id) && b(Long id, String name) -> false
    * a(String name) && b(String name2) -> true
    * a(List list) && b(List<String> strings) -> true
    * a(List<String> strings) b(List list) -> false
    * }</pre>
    */
   boolean sameParameterTypes(C_Method method);

   /**
    * The java language and the java virtual machine have different specification. Bridge Methods are created to bridge that gap
    */
   boolean isBridge();

   /**
    * Can be annotated using annotations with {@link ElementType#TYPE_USE}
    */
   R_Result getResult();
}
