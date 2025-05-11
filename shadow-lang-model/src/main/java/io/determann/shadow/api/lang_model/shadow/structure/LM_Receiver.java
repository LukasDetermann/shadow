package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.LM_Annotationable;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.shadow.structure.C_Receiver;

public interface LM_Receiver
      extends C_Receiver,
              LM_Annotationable
{
   LM_Type getType();
}
