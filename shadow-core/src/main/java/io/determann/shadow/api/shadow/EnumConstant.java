package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Documented;

public interface EnumConstant extends Variable,
                                      Documented
{
   @Override
   Enum getSurrounding();
}
