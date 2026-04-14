package com.derivandi.shadow.structure;

import com.derivandi.api.Ap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.derivandi.api.test.ProcessorTest.processorTest;

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
