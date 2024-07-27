package io.determann.shadow.api.lang_model.shadow.directive;

import io.determann.shadow.api.shadow.directive.Uses;
import io.determann.shadow.api.shadow.type.Declared;

/**
 * Uses a Service of another module
 *
 * @see ProvidesLangModel
 */
public non-sealed interface UsesLangModel extends DirectiveLangModel,
                                                  Uses
{
   Declared getService();
}
