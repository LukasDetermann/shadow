package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.Nameable;

/**
 * represents the generic parameter at a class, method, constructor etc.
 */
public interface Generic extends Shadow,
                                 Nameable,
                                 Annotationable
{
}
