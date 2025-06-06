package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.shadow.*;
import io.determann.shadow.api.shadow.modifier.C_AccessModifiable;
import io.determann.shadow.api.shadow.modifier.C_StrictfpModifiable;

/**
 * Anything that can be a file.
 */
public interface C_Declared
      extends C_Type,
              C_Annotationable,
              C_AccessModifiable,
              C_StrictfpModifiable,
              C_Nameable,
              C_QualifiedNameable,
              C_ModuleEnclosed,
              C_Documented
{
}
