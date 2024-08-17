package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.shadow.type.Void;

class VoidTest extends ShadowTest<Void>
{
   VoidTest()
   {
      super(context -> context.getConstants().getVoid());
   }
}
