package com.derivandi.javadoc;

import com.derivandi.api.Ap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.derivandi.api.test.ProcessorTest.processorTest;

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