package io.determann.shadow.api.lang_model.shadow.directive;

import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.shadow.directive.C_Provides;

import java.util.List;

/**
 * Provides a {@link #getService()} to other modules internally using {@link #getImplementations()}
 *
 * @see LM_Uses
 */
public non-sealed interface LM_Provides extends LM_Directive,
                                                C_Provides
{
   /**
    * a service to provide to other modules
    */
   LM_Declared getService();

   /**
    * Implementations of the provided service
    */
   List<LM_Declared> getImplementations();
}
