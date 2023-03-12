package org.determann.shadow.api.shadow;

import org.determann.shadow.api.Annotationable;
import org.determann.shadow.api.DeclaredHolder;
import org.determann.shadow.api.QualifiedNameable;
import org.determann.shadow.api.shadow.module.Directive;
import org.determann.shadow.api.shadow.module.DirectiveConsumer;
import org.determann.shadow.api.shadow.module.DirectiveMapper;

import javax.lang.model.element.ModuleElement;
import javax.lang.model.type.NoType;
import java.util.List;

public interface Module extends Shadow<NoType>,
                                QualifiedNameable<ModuleElement>,
                                Annotationable<ModuleElement>,
                                DeclaredHolder
{
   List<Package> getPackages();

   /**
    * can everybody use reflection on this module?
    */
   boolean isOpen();

   boolean isUnnamed();

   boolean isAutomatic();

   /**
    * Relations between modules
    */
   List<Directive> getDirectives();

   <T> List<T> mapDirectives(DirectiveMapper<T> mapper);

   void consumeDirectives(DirectiveConsumer consumer);

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
