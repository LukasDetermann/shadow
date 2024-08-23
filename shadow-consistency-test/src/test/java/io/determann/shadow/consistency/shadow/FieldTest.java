package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Field;
import io.determann.shadow.api.lang_model.shadow.type.LM_Class;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldTest extends VariableTest<LM_Field>
{
   @Test
   void testGetSurrounding()
   {
      ProcessorTest.process(context ->
                            {
                               LM_Class aClass = context.getClassOrThrow("FieldExample");
                               assertEquals(aClass, aClass.getFieldOrThrow("ID").getSurrounding());
                            })
                   .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                   .compile();
   }

   @Test
   void testIsConstant()
   {
      ProcessorTest.process(context -> assertTrue(context.getClassOrThrow("FieldExample")
                                                          .getFieldOrThrow("ID")
                                                          .isConstant()))
                   .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                   .compile();
   }

   @Test
   void testGetConstantValue()
   {
      ProcessorTest.process(context -> assertEquals(2, context.getClassOrThrow("FieldExample")
                                                                  .getFieldOrThrow("ID")
                                                                  .getConstantValue()))
                   .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                   .compile();
   }
}
