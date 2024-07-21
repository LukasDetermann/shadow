package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.modifier.AccessModifiableReflection;
import io.determann.shadow.api.shadow.structure.Constructor;

public interface ConstructorReflection extends Constructor,
                                               ExecutableReflection,
                                               AccessModifiableReflection
{
}
