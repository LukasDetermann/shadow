package io.determann.shadow.api.shadow.directive;

import io.determann.shadow.api.shadow.type.Declared;

/**
 * Uses a Service of another module
 *
 * @see Provides
 */
public non-sealed interface Uses extends Directive
{
   Declared getService();
}
