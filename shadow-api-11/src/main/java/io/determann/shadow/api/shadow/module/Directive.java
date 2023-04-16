package io.determann.shadow.api.shadow.module;

import io.determann.shadow.api.ApiHolder;

/**
 * Relation between modules
 */
public interface Directive extends ApiHolder
{
   DirectiveKind getKind();
}
