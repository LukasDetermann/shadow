package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumConstantTest extends VariableTest<EnumConstant>
{
   EnumConstantTest()
   {
      super(shadowApi -> query(shadowApi.getEnumOrThrow("java.lang.annotation.RetentionPolicy")).getEnumConstantOrThrow("SOURCE"));
   }

   @Test
   void testGetSurrounding()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Enum anEnum = shadowApi.getEnumOrThrow("java.lang.annotation.RetentionPolicy");
                               assertEquals(anEnum, query(query(anEnum).getEnumConstantOrThrow("SOURCE")).getSurrounding());
                            })
                   .compile();
   }
}
