package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.shadow.type.C_Null;

class NullTest extends ShadowTest<C_Null>
{
   NullTest()
   {
      super(shadowApi -> shadowApi.getConstants().getNull());
   }
}
