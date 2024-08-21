package io.determann.shadow.api.reflection.shadow.directive;

import io.determann.shadow.api.reflection.shadow.type.R_Declared;
import io.determann.shadow.api.shadow.directive.C_Provides;

import java.util.List;

/**
 * Provides a {@link #getService()} to other modules internally using {@link #getImplementations()}
 *
 * @see R_Uses
 */
public non-sealed interface R_Provides extends R_Directive,
                                               C_Provides
{
   /**
    * a service to provide to other modules
    */
   R_Declared getService();

   /**
    * Implementations of the provided service
    */
   List<R_Declared> getImplementations();
}
