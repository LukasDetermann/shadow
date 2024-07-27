package io.determann.shadow.api.reflection.shadow.directive;

/**
 * Relation between modules
 */
public sealed interface DirectiveReflection permits ExportsReflection,
                                                    OpensReflection,
                                                    ProvidesReflection,
                                                    RequiresReflection,
                                                    UsesReflection
{
}
