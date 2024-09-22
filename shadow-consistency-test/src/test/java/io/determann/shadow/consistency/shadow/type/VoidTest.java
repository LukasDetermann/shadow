package io.determann.shadow.consistency.shadow.type;

import io.determann.shadow.api.shadow.type.C_Void;

class VoidTest extends TypeTest<C_Void>
{
   VoidTest()
   {
      super(context -> context.getConstants().getVoid());
   }
}