package io.determann.shadow.api.lang_model.shadow;

import io.determann.shadow.api.lang_model.shadow.structure.ModuleLangModel;
import io.determann.shadow.api.shadow.ModuleEnclosed;

public interface ModuleEnclosedLangModel extends ModuleEnclosed
{
   ModuleLangModel getModule();
}