package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.LM_Annotationable;
import io.determann.shadow.api.lang_model.shadow.type.LM_Shadow;
import io.determann.shadow.api.shadow.structure.C_Return;

public interface LM_Return extends C_Return,
                                   LM_Annotationable
{
   LM_Shadow getType();
}
