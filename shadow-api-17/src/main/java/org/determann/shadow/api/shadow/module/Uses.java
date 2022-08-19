package org.determann.shadow.api.shadow.module;

import org.determann.shadow.api.shadow.Declared;

/**
 * Uses a Service of another module
 * @see Provides
 */
public interface Uses extends Directive
{
   Declared getService();
}
