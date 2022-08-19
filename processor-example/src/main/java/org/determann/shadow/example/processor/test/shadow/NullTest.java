package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Null;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;

public class NullTest extends ShadowTest<Null>
{
   protected NullTest()
   {
      super(() -> SHADOW_API.getConstants().getNull());
   }
}
