package io.determann.shadow.api.reflection.shadow.directive;

import io.determann.shadow.api.reflection.shadow.type.DeclaredReflection;
import io.determann.shadow.api.shadow.directive.Uses;

/**
 * Uses a Service of another module
 *
 * @see ProvidesReflection
 */
public non-sealed interface UsesReflection extends DirectiveReflection,
                                                   Uses
{
   DeclaredReflection getService();
}
