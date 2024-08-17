package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.modifier.*;
import io.determann.shadow.api.shadow.structure.Method;

public interface MethodLangModel extends Method,
                                         ExecutableLangModel,
                                         StaticModifiableLangModel,
                                         DefaultModifiableLangModel,
                                         AccessModifiableLangModel,
                                         AbstractModifiableLangModel,
                                         FinalModifiableLangModel,
                                         StrictfpModifiableLangModel,
                                         NativeModifiableLangModel
{
   boolean overrides(Method method);

   boolean overwrittenBy(Method method);

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
   boolean sameParameterTypes(Method method);

   /**
    * The java language and the java virtual machine have different specification. Bridge Methods are created to bridge that gap
    */
   boolean isBridge();
}
