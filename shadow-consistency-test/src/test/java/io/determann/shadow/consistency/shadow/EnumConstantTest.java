package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.structure.EnumConstantLangModel;
import io.determann.shadow.api.lang_model.shadow.type.EnumLangModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumConstantTest extends VariableTest<EnumConstantLangModel>
{
   EnumConstantTest()
   {
      super(context -> context.getEnumOrThrow("java.lang.annotation.RetentionPolicy").getEnumConstantOrThrow("SOURCE"));
   }

   @Test
   void testGetSurrounding()
   {
      ProcessorTest.process(context ->
                            {
                               EnumLangModel anEnum = context.getEnumOrThrow("java.lang.annotation.RetentionPolicy");
                               assertEquals(anEnum, anEnum.getEnumConstantOrThrow("SOURCE").getSurrounding());
                            })
                   .compile();
   }
}
