package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Void;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;

public class VoidTest extends ShadowTest<Void>
{
   protected VoidTest()
   {
      super(() -> SHADOW_API.getConstants().getVoid());
   }
}
