package io.determann.shadow.tck.internal.shadow.type;

import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.api.shadow.type.C_Wildcard;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WildcardTest
{
   @Test
   void testGetExtends()
   {
      withSource("BoundsExample.java", """
            public class BoundsExample {
               public static void extendsExample(java.util.List<? extends Number> numbers) {}
            }
            """)
            .test(implementation ->
                  {
                     C_Class boundsExample = requestOrThrow(implementation, GET_CLASS, "BoundsExample");
                     C_Method extendsExample = requestOrThrow(boundsExample, DECLARED_GET_METHOD, "extendsExample").get(0);
                     C_Parameter parameter = requestOrThrow(extendsExample, EXECUTABLE_GET_PARAMETER, "numbers");
                     C_Interface parameterType = ((C_Interface) requestOrThrow(parameter, VARIABLE_GET_TYPE));
                     C_Wildcard wildcard = (C_Wildcard) requestOrThrow(parameterType, INTERFACE_GET_GENERIC_TYPES).get(0);

                     assertEquals(requestOrThrow(implementation, GET_CLASS, "java.lang.Number"),
                                  requestOrThrow(wildcard, WILDCARD_GET_EXTENDS));
                  });
   }

   @Test
   void testGetSupper()
   {
      withSource("BoundsExample.java", """
            public class BoundsExample {
               public static void superExample(java.util.List<? super Number> numbers) {}
            }
            """)
            .test(implementation ->
                  {
                     C_Class boundsExample = requestOrThrow(implementation, GET_CLASS, "BoundsExample");
                     C_Method extendsExample = requestOrThrow(boundsExample, DECLARED_GET_METHOD, "superExample").get(0);
                     C_Parameter parameter = requestOrThrow(extendsExample, EXECUTABLE_GET_PARAMETER, "numbers");
                     C_Interface parameterType = ((C_Interface) requestOrThrow(parameter, VARIABLE_GET_TYPE));
                     C_Wildcard wildcard = (C_Wildcard) requestOrThrow(parameterType, INTERFACE_GET_GENERIC_TYPES).get(0);

                     assertEquals(requestOrThrow(implementation, GET_CLASS, "java.lang.Number"),
                                  requestOrThrow(wildcard, WILDCARD_GET_SUPER));
                  });
   }
}
