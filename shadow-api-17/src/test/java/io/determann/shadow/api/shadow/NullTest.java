package io.determann.shadow.api.shadow;

class NullTest extends ShadowTest<Null>
{
   NullTest()
   {
      super(shadowApi -> shadowApi.getConstants().getNull());
   }
}
