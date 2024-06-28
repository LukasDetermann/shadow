package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.shadow.type.Null;

class NullTest extends ShadowTest<Null>
{
   NullTest()
   {
      super(shadowApi -> shadowApi.getConstants().getNull());
   }
}
