package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.shadow.C_Nameable;

/**
 * This represents the broadest definition of a property. only a {@link #getGetter()} is mandatory
 * <p>
 * The search for properties starts with the getter {@link #getGetter()}
 */
public interface C_Property extends C_Nameable
{
}
