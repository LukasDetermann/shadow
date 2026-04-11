package io.determann.shadow.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.test.ProcessorTest.processorTest;

class EnumConstantTest
{
   @Test
   void getSurrounding()
   {
      processorTest().process(context ->
                              {
                                 Ap.Enum retentionPolicy = context.getEnumOrThrow("java.lang.annotation.RetentionPolicy");
                                 Ap.EnumConstant source = retentionPolicy.getEnumConstantOrThrow("SOURCE");
                                 Assertions.assertEquals(retentionPolicy, source.getSurrounding());
                              });
   }
}
