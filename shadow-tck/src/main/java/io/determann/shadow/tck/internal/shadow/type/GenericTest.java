package io.determann.shadow.tck.internal.shadow.type;

import io.determann.shadow.api.C;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericTest
{
   @Test
   void getExtends()
   {
      withSource("GenericsExample.java",
                 "public class GenericsExample<T extends Number>{}")
            .test(implementation ->
                  {
                     C.Class number = requestOrThrow(implementation, GET_CLASS, "java.lang.Number");
                     C.Class genericsExample = requestOrThrow(implementation, GET_CLASS, "GenericsExample");
                     C.Generic generic = (C.Generic) requestOrThrow(genericsExample, CLASS_GET_GENERIC_TYPES).get(0);
                     assertEquals(number, requestOrThrow(generic, GENERIC_GET_BOUND));
                  });
   }

   @Test
   void getEnclosing()
   {
      withSource("GenericsExample.java",
                 "public class GenericsExample<T extends Number>{}")
            .test(implementation ->
                  {
                     C.Class genericsExample = requestOrThrow(implementation, GET_CLASS, "GenericsExample");
                     C.Generic generic = (C.Generic) requestOrThrow(genericsExample, CLASS_GET_GENERIC_TYPES).get(0);
                     assertEquals(genericsExample, requestOrThrow(generic, GENERIC_GET_ENCLOSING));
                  });
   }
}
