package io.determann.shadow.tck.internal.shadow.type;

import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Generic;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
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
                     C_Class number = requestOrThrow(implementation, GET_CLASS, "java.lang.Number");
                     C_Class genericsExample = requestOrThrow(implementation, GET_CLASS, "GenericsExample");
                     C_Generic generic = (C_Generic) requestOrThrow(genericsExample, CLASS_GET_GENERIC_TYPES).get(0);
                     assertEquals(number, requestOrThrow(generic, GENERIC_GET_EXTENDS));
                  });
   }

   @Test
   void getEnclosing()
   {
      withSource("GenericsExample.java",
                 "public class GenericsExample<T extends Number>{}")
            .test(implementation ->
                  {
                     C_Class genericsExample = requestOrThrow(implementation, GET_CLASS, "GenericsExample");
                     C_Generic generic = (C_Generic) requestOrThrow(genericsExample, CLASS_GET_GENERIC_TYPES).get(0);
                     assertEquals(genericsExample, requestOrThrow(generic, GENERIC_GET_ENCLOSING));
                  });
   }
}
