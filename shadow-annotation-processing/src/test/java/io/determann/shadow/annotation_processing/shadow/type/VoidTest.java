package io.determann.shadow.annotation_processing.shadow.type;

import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.test.ProcessorTest.processorTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VoidTest
{
   @Test
   void nonNull()
   {
      processorTest().process(context -> assertNotNull(context.getConstants().getVoid()));
   }
}

