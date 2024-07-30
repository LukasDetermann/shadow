package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.shadow.Nameable;

/**
 * represents primitive types, but not there wrapper classes. for example int, long, short
 */
public interface Primitive extends Shadow,
                                   Nameable
{
}
