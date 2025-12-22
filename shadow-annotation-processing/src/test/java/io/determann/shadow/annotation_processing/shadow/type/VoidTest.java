package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class VoidTest
{
   @Test
   void nonNull()
   {
      ProcessorTest.process(context -> assertNotNull(context.getConstants().getVoid())).compile();
   }
}

