package io.determann.shadow.api.reflection.shadow.directive;

import io.determann.shadow.api.reflection.shadow.type.DeclaredReflection;
import io.determann.shadow.api.shadow.directive.Provides;

import java.util.List;

/**
 * Provides a {@link #getService()} to other modules internally using {@link #getImplementations()}
 *
 * @see UsesReflection
 */
public non-sealed interface ProvidesReflection extends DirectiveReflection,
                                                       Provides
{
   /**
    * a service to provide to other modules
    */
   DeclaredReflection getService();

   /**
    * Implementations of the provided service
    */
   List<DeclaredReflection> getImplementations();
}
