package io.determann.shadow.api.shadow.module;

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
