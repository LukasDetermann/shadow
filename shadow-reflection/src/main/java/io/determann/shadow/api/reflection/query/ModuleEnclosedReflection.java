package io.determann.shadow.api.reflection.query;

import io.determann.shadow.api.ModuleEnclosed;
import io.determann.shadow.api.shadow.Module;

public interface ModuleEnclosedReflection extends ModuleEnclosed
{
   Module getModule();
}