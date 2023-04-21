package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.modifier.*;

import javax.lang.model.element.ExecutableElement;

public interface Method extends Executable,
                                Annotationable<ExecutableElement>,
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
    * Do both methods have the same parameter types in the same order?
    * a() && b() -> true
    * a(String name) && b() -> false
    * a(String name, Long id) && b(Long id, String name) -> false
    * a(String name) && b(String name2) -> true
    * a(List list) && b(List<String> strings) -> true
    * a(List<String> strings) b(List list) -> false
    */
   boolean sameParameterTypes(Method method);

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
