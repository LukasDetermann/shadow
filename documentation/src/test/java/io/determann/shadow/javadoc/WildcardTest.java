package io.determann.shadow.javadoc;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WildcardTest
{
   @Test
   void contains()
   {
      ProcessorTest.process(context ->
                            {
                               //@start region="Wildcard.contains"
                               AP.Class number = context.getClassOrThrow("java.lang.Number");
                               AP.Wildcard wildcard = number.asExtendsWildcard();
                               Assertions.assertTrue(wildcard.contains(context.getClassOrThrow("java.lang.Long")));//@highlight substring="contains"
                               //@end
                            })
                   .compile();
   }
}