package io.determann.shadow.api.lang_model.query;

import io.determann.shadow.api.ModuleEnclosed;
import io.determann.shadow.api.shadow.Module;

public interface ModuleEnclosedLangModel extends ModuleEnclosed
{
   Module getModule();
}