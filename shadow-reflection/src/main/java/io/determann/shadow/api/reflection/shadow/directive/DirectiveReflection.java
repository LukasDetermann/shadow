package io.determann.shadow.api.reflection.shadow.directive;

import io.determann.shadow.api.shadow.directive.Directive;

/**
 * Relation between modules
 */
public sealed interface DirectiveReflection extends Directive permits ExportsReflection,
                                                                      OpensReflection,
                                                                      ProvidesReflection,
                                                                      RequiresReflection,
                                                                      UsesReflection
{
}
