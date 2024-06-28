package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.shadow.type.Void;

class VoidTest extends ShadowTest<Void>
{
   VoidTest()
   {
      super(shadowApi -> shadowApi.getConstants().getVoid());
   }
}
