package io.determann.shadow.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.test.ProcessorTest.processorTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldTest
{
   @Test
   void getSurrounding()
   {
      processorTest().withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                     .process(context ->
                              {
                                 Ap.Class example = context.getClassOrThrow("FieldExample");
                                 Ap.Field field = example.getFieldOrThrow("ID");
                                 assertEquals(example, field.getSurrounding());
                              });
   }

   @Test
   void isConstant()
   {
      processorTest().withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                     .process(context ->
                              {
                                 Ap.Class example = context.getClassOrThrow("FieldExample");
                                 Ap.Field field = example.getFieldOrThrow("ID");
                                 assertTrue(field.isConstant());
                              });
   }

   @Test
   void getConstantValue()
   {
      processorTest().withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                     .process(context ->
                              {
                                 Ap.Class example = context.getClassOrThrow("FieldExample");
                                 Ap.Field field = example.getFieldOrThrow("ID");
                                 assertEquals(2, field.getConstantValue());
                              });
   }
}
