package io.determann.shadow.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EnumConstantTest
{
   @Test
   void getSurrounding()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Enum retentionPolicy = context.getEnumOrThrow("java.lang.annotation.RetentionPolicy");
                               Ap.EnumConstant source = retentionPolicy.getEnumConstantOrThrow("SOURCE");
                               Assertions.assertEquals(retentionPolicy, source.getSurrounding());
                            })
                   .compile();
   }
}
