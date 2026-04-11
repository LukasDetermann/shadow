package io.determann.shadow.annotation_processing.javadoc;

import io.determann.shadow.api.annotation_processing.Ap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.test.ProcessorTest.processorTest;

public class WildcardTest
{
   @Test
   void contains()
   {
      processorTest().process(context ->
                              {
                                 //@start region="Wildcard.contains"
                                 Ap.Class number = context.getClassOrThrow("java.lang.Number");
                                 Ap.Wildcard wildcard = number.asExtendsWildcard();
                                 Assertions.assertTrue(wildcard.contains(context.getClassOrThrow("java.lang.Long")));//@highlight substring="contains"
                                 //@end
                              });
   }
}