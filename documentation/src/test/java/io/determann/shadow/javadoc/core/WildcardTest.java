package io.determann.shadow.javadoc.core;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Wildcard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.lang_model.query.LangModelQueries.query;

public class WildcardTest
{
   @Test
   void contains()
   {
      ProcessorTest.process(context ->
                            {
                               //@start region="Wildcard.contains"
                               Class number = context.getClassOrThrow("java.lang.Number");
                               Wildcard wildcard = context.asExtendsWildcard(number);
                               Assertions.assertTrue(query(wildcard).contains(context.getClassOrThrow("java.lang.Long")));//@highlight substring="contains"
                               //@end
                            })
                   .compile();
   }
}