package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.shadow.C_Nameable;

/**
 * represents primitive types, but not there wrapper classes. for example int, long, short
 */
public interface C_Primitive extends C_Shadow,
                                     C_Nameable
{
}
