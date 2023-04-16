package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.DeclaredHolder;
import io.determann.shadow.api.QualifiedNameable;

import javax.lang.model.element.PackageElement;
import javax.lang.model.type.NoType;
import java.util.List;

public interface Package extends Shadow<NoType>,
                                 QualifiedNameable<PackageElement>,
                                 Annotationable<PackageElement>,
                                 DeclaredHolder
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
