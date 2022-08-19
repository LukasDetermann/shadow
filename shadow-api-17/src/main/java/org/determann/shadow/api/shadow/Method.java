package org.determann.shadow.api.shadow;

import org.determann.shadow.api.Annotationable;
import org.determann.shadow.api.modifier.*;

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

   /**
    * <pre>
    * {@code
    * public abstract class Outer {
    *    public abstract void foo();
    *
    *    public static class Inner extends Outer {
    *       @Override
    *       public void foo() { }
    *    }
    * }
    * }
    * </pre>
    * <li> Outer.foo.isSubSignatureOf(Inner.foo) == true
    * <li> Inner.foo.isSubSignatureOf(Outer.foo) == true
    */
   boolean isSubSignatureOf(Method method);

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
