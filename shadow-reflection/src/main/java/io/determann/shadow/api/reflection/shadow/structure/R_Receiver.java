package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.R_Annotationable;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.structure.C_Receiver;

public interface R_Receiver

      extends C_Receiver,
              R_Annotationable
{
   R_Type getType();
}
