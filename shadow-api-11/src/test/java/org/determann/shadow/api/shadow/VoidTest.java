package org.determann.shadow.api.shadow;


class VoidTest extends ShadowTest<Void>
{
   VoidTest()
   {
      super(shadowApi -> shadowApi.getConstants().getVoid());
   }
}
