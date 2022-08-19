package org.determann.shadow.api.shadow;

import org.determann.shadow.api.Annotationable;
import org.determann.shadow.api.modifier.AccessModifiable;

import javax.lang.model.element.ExecutableElement;

public interface Constructor extends Executable,
                                     AccessModifiable,
                                     Annotationable<ExecutableElement>
{
   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
