package io.determann.shadow.api.reflection.shadow.directive;

import io.determann.shadow.api.shadow.directive.C_Directive;

/**
 * Relation between modules
 */
public sealed interface R_Directive extends C_Directive permits R_Exports,
                                                                R_Opens,
                                                                R_Provides,
                                                                R_Requires,
                                                                R_Uses
{
}
