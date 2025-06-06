package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.shadow.C_Annotationable;
import io.determann.shadow.api.shadow.C_Erasable;
import io.determann.shadow.api.shadow.C_Nameable;

/**
 * represents the generic parameter at a class, method, constructor etc.
 */
public interface C_Generic
      extends C_Type,
              C_Nameable,
              C_Annotationable,
              C_Erasable
{
}
