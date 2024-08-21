package io.determann.shadow.api.lang_model.shadow;

import io.determann.shadow.api.lang_model.shadow.structure.LM_Module;
import io.determann.shadow.api.shadow.C_ModuleEnclosed;

public interface LM_ModuleEnclosed extends C_ModuleEnclosed
{
   LM_Module getModule();
}