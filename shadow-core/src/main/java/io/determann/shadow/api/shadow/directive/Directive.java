package io.determann.shadow.api.shadow.directive;

/**
 * Relation between modules
 */
public sealed interface Directive permits Exports,
                                          Opens,
                                          Provides,
                                          Requires,
                                          Uses
{
}
