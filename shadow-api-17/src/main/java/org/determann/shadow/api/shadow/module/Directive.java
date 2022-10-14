package org.determann.shadow.api.shadow.module;

import org.determann.shadow.api.ApiHolder;

/**
 * Relation between modules
 */
public interface Directive extends ApiHolder
{
   DirectiveKind getKind();
}
