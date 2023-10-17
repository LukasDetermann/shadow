package io.determann.shadow.api.shadow.module;

import io.determann.shadow.impl.annotation_processing.ApiHolder;

/**
 * Relation between modules
 */
public interface Directive extends ApiHolder
{
   DirectiveKind getKind();
}
