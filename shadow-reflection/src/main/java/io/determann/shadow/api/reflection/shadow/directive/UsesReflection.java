package io.determann.shadow.api.reflection.shadow.directive;

import io.determann.shadow.api.shadow.directive.Uses;
import io.determann.shadow.api.shadow.type.Declared;

/**
 * Uses a Service of another module
 *
 * @see ProvidesReflection
 */
public non-sealed interface UsesReflection extends DirectiveReflection,
                                                   Uses
{
   Declared getService();
}
