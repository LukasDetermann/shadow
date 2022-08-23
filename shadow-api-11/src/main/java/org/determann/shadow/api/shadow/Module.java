package org.determann.shadow.api.shadow;

import org.determann.shadow.api.Annotationable;
import org.determann.shadow.api.QualifiedNameable;
import org.determann.shadow.api.shadow.module.Directive;
import org.determann.shadow.api.shadow.module.DirectiveConsumer;
import org.determann.shadow.api.shadow.module.DirectiveMapper;

import javax.lang.model.element.ModuleElement;
import javax.lang.model.type.NoType;
import java.util.List;

public interface Module extends Shadow<NoType>,
                                QualifiedNameable<ModuleElement>,
                                Annotationable<ModuleElement>
{
   List<Package> getPackages();

   /**
    * returns a {@link Declared} from this module
    *
    * @param qualifiedName e.g. org.example.MyClass
    */
   Declared getDeclared(String qualifiedName);

   /**
    * @see #getDeclared(String)
    */
   Annotation getAnnotation(String qualifiedName);

   /**
    * @see #getDeclared(String)
    */
   Class getClass(String qualifiedName);

   /**
    * @see #getDeclared(String)
    */
   Enum getEnum(String qualifiedName);

   /**
    * @see #getDeclared(String)
    */
   Interface getInterface(String qualifiedName);

   /**
    * can everybody use reflection on this module?
    */
   boolean isOpen();

   boolean isUnnamed();

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
