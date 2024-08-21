package io.determann.shadow.api.lang_model.shadow.directive;

import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.shadow.directive.C_Uses;

/**
 * Uses a Service of another module
 *
 * @see LM_Provides
 */
public non-sealed interface LM_Uses extends LM_Directive,
                                            C_Uses
{
   LM_Declared getService();
}
