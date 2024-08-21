package io.determann.shadow.api.lang_model.shadow.directive;

import io.determann.shadow.api.shadow.directive.C_Directive;

/**
 * Relation between modules
 */
public sealed interface LM_Directive extends C_Directive permits LM_Exports,
                                                                 LM_Opens,
                                                                 LM_Provides,
                                                                 LM_Requires,
                                                                 LM_Uses
{
}
