package io.determann.shadow.api.reflection.shadow.directive;

import io.determann.shadow.api.reflection.shadow.type.R_Declared;
import io.determann.shadow.api.shadow.directive.C_Uses;

/**
 * Uses a Service of another module
 *
 * @see R_Provides
 */
public non-sealed interface R_Uses extends R_Directive,
                                           C_Uses
{
   R_Declared getService();
}
