package org.determann.shadow.api.shadow;

import org.determann.shadow.api.Annotationable;
import org.determann.shadow.api.QualifiedNameable;

import javax.lang.model.element.PackageElement;
import javax.lang.model.type.NoType;
import java.util.List;

public interface Package extends Shadow<NoType>,
                                 QualifiedNameable<PackageElement>,
                                 Annotationable<PackageElement>
{
   /**
    * returns everything in this package
    */
   List<Declared> getContent();

   /**
    * Unnamed packages are intend for small snips of code like jShell and not seen in regular projects
    */
   boolean isUnnamed();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
