package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.modifier.*;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.shadow.structure.C_Method;

public non-sealed interface LM_Method extends C_Method,
                                              LM_Executable,
                                              LM_StaticModifiable,
                                              LM_DefaultModifiable,
                                              LM_AccessModifiable,
                                              LM_AbstractModifiable,
                                              LM_FinalModifiable,
                                              LM_StrictfpModifiable,
                                              LM_NativeModifiable
{
   LM_Type getReturnType();

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
}
