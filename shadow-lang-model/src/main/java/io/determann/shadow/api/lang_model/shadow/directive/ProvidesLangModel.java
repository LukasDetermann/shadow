package io.determann.shadow.api.lang_model.shadow.directive;

import io.determann.shadow.api.shadow.directive.Provides;
import io.determann.shadow.api.shadow.type.Declared;

import java.util.List;

/**
 * Provides a {@link #getService()} to other modules internally using {@link #getImplementations()}
 *
 * @see UsesLangModel
 */
public non-sealed interface ProvidesLangModel extends DirectiveLangModel,
                                                      Provides
{
   /**
    * a service to provide to other modules
    */
   Declared getService();

   /**
    * Implementations of the provided service
    */
   List<Declared> getImplementations();
}
