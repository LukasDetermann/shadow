package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.type.Class;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldTest extends VariableTest<Field>
{
   FieldTest()
   {
      super(shadowApi -> query(shadowApi.getClassOrThrow("java.lang.String")).getFieldOrThrow("value"));
   }

   @Test
   void testGetSurrounding()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Class aClass = shadowApi.getClassOrThrow("FieldExample");
                               assertEquals(aClass, query(query(aClass).getFieldOrThrow("ID")).getSurrounding());
                            })
                   .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                   .compile();
   }

   @Test
   void testIsConstant()
   {
      ProcessorTest.process(shadowApi -> assertTrue(query(query(shadowApi.getClassOrThrow("FieldExample"))
                                                          .getFieldOrThrow("ID"))
                                                          .isConstant()))
                   .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                   .compile();
   }

   @Test
   void testGetConstantValue()
   {
      ProcessorTest.process(shadowApi -> assertEquals(2, query(query(shadowApi.getClassOrThrow("FieldExample"))
                                                                  .getFieldOrThrow("ID"))
                                                                  .getConstantValue()))
                   .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                   .compile();
   }
}
