package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.*;

public interface Method extends Executable,
                                StaticModifiable,
                                DefaultModifiable,
                                AccessModifiable,
                                AbstractModifiable,
                                FinalModifiable,
                                StrictfpModifiable,
                                NativeModifiable
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
}
