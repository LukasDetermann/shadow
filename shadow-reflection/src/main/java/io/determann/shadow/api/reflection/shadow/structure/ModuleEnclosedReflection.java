package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.shadow.ModuleEnclosed;
import io.determann.shadow.api.shadow.structure.Module;

public interface ModuleEnclosedReflection extends ModuleEnclosed
{
   Module getModule();
}