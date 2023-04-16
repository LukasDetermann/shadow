package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.DeclaredHolder;
import io.determann.shadow.api.QualifiedNameable;
import io.determann.shadow.api.shadow.module.Directive;
import io.determann.shadow.api.shadow.module.DirectiveConsumer;
import io.determann.shadow.api.shadow.module.DirectiveMapper;

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
