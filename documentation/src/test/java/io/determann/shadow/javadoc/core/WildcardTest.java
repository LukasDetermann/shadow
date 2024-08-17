package io.determann.shadow.javadoc.core;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.type.ClassLangModel;
import io.determann.shadow.api.lang_model.shadow.type.WildcardLangModel;
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
                               ClassLangModel number = context.getClassOrThrow("java.lang.Number");
                               WildcardLangModel wildcard = context.asExtendsWildcard(number);
                               Assertions.assertTrue(wildcard.contains(context.getClassOrThrow("java.lang.Long")));//@highlight substring="contains"
                               //@end
                            })
                   .compile();
   }
}