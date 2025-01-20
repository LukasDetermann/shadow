package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.LM_Annotationable;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.shadow.structure.C_Result;

public interface LM_Result

      extends C_Result,
              LM_Annotationable
{
   LM_Type getType();
}
