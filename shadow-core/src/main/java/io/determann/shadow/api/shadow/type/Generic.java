package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.shadow.Annotationable;
import io.determann.shadow.api.shadow.Nameable;

/**
 * represents the generic parameter at a class, method, constructor etc.
 */
public interface Generic extends Shadow,
                                 Nameable,
                                 Annotationable
{
}
