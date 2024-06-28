package io.determann.shadow.api.lang_model.shadow;

import io.determann.shadow.api.shadow.ModuleEnclosed;
import io.determann.shadow.api.shadow.structure.Module;

public interface ModuleEnclosedLangModel extends ModuleEnclosed
{
   Module getModule();
}