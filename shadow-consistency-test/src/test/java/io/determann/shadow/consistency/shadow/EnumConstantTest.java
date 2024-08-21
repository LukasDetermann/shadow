package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.structure.LM_EnumConstant;
import io.determann.shadow.api.lang_model.shadow.type.LM_Enum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumConstantTest extends VariableTest<LM_EnumConstant>
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
                               LM_Enum anEnum = context.getEnumOrThrow("java.lang.annotation.RetentionPolicy");
                               assertEquals(anEnum, anEnum.getEnumConstantOrThrow("SOURCE").getSurrounding());
                            })
                   .compile();
   }
}
