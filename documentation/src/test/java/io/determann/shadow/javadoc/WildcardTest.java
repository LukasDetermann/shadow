package io.determann.shadow.javadoc;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.type.LM_Class;
import io.determann.shadow.api.lang_model.shadow.type.LM_Wildcard;
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
                               LM_Class number = context.getClassOrThrow("java.lang.Number");
                               LM_Wildcard wildcard = number.asExtendsWildcard();
                               Assertions.assertTrue(wildcard.contains(context.getClassOrThrow("java.lang.Long")));//@highlight substring="contains"
                               //@end
                            })
                   .compile();
   }
}