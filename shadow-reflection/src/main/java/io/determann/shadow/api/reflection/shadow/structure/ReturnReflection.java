package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.shadow.structure.Return;
import io.determann.shadow.api.shadow.type.Shadow;

public interface ReturnReflection extends Return
{
   Shadow getType();
}
