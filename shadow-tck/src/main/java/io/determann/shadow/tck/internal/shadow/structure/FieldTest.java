package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.C;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldTest
{
   @Test
   void getSurrounding()
   {
      withSource("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
            .test(implementation ->
                  {
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "FieldExample");
                     C.Field field = requestOrThrow(example, DECLARED_GET_FIELD, "ID");
                     assertEquals(example, requestOrThrow(field, FIELD_GET_SURROUNDING));
                  });
   }

   @Test
   void isConstant()
   {
      withSource("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
            .test(implementation ->
                  {
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "FieldExample");
                     C.Field field = requestOrThrow(example, DECLARED_GET_FIELD, "ID");
                     assertTrue(requestOrThrow(field, FIELD_IS_CONSTANT));
                  });
   }

   @Test
   void getConstantValue()
   {
      withSource("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
            .test(implementation ->
                  {
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "FieldExample");
                     C.Field field = requestOrThrow(example, DECLARED_GET_FIELD, "ID");
                     assertEquals(2, requestOrThrow(field, FIELD_GET_CONSTANT_VALUE));
                  });
   }
}
