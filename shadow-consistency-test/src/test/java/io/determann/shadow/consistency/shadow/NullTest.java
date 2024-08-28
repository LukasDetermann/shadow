package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.shadow.type.C_Null;

class NullTest extends TypeTest<C_Null>
{
   NullTest()
   {
      super(context -> context.getConstants().getNull());
   }
}
