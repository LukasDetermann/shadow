package io.determann.shadow.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldTest
{
   @Test
   void getSurrounding()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class example = context.getClassOrThrow("FieldExample");
                               Ap.Field field = example.getFieldOrThrow("ID");
                               assertEquals(example, field.getSurrounding());
                            })
                   .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                   .compile();
   }

   @Test
   void isConstant()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class example = context.getClassOrThrow("FieldExample");
                               Ap.Field field = example.getFieldOrThrow("ID");
                               assertTrue(field.isConstant());
                            })
                   .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                   .compile();
   }

   @Test
   void getConstantValue()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class example = context.getClassOrThrow("FieldExample");
                               Ap.Field field = example.getFieldOrThrow("ID");
                               assertEquals(2, field.getConstantValue());
                            })
                   .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                   .compile();
   }
}
