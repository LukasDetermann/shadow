package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.api.shadow.type.C_Wildcard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.withSource;

class WildcardRendererTest
{
   @Test
   void typeExtends()
   {
      withSource("BoundsExample.java", """
            public class BoundsExample {
               public static void extendsExample(java.util.List<? extends Number> numbers) {}
            }
            """)
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "BoundsExample");
                     C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "extendsExample").get(0);
                     C_Parameter parameter = requestOrThrow(method, EXECUTABLE_GET_PARAMETERS).get(0);
                     C_Interface type = (C_Interface) requestOrThrow(parameter, VARIABLE_GET_TYPE);
                     C_Wildcard wildcard = ((C_Wildcard) requestOrThrow(type, INTERFACE_GET_GENERIC_TYPES).get(0));
                     Assertions.assertEquals("? extends Number", render(DEFAULT, wildcard).type());
                  });
   }

   @Test
   void typeSuper()
   {
      withSource("BoundsExample.java", """
            public class BoundsExample {
                public static void superExample(java.util.List<? super Number> numbers) {}
            }
            """)
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "BoundsExample");
                     C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "superExample").get(0);
                     C_Parameter parameter = requestOrThrow(method, EXECUTABLE_GET_PARAMETERS).get(0);
                     C_Interface type = (C_Interface) requestOrThrow(parameter, VARIABLE_GET_TYPE);
                     C_Wildcard wildcard = ((C_Wildcard) requestOrThrow(type, INTERFACE_GET_GENERIC_TYPES).get(0));
                     Assertions.assertEquals("? super Number", render(DEFAULT, wildcard).type());
                  });
   }

   @Test
   void typeUnbound()
   {
      withSource("BoundsExample.java", """
            public class BoundsExample {
                public static void unboundExample(java.util.List<?> things) {}
            }
            """)
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "BoundsExample");
                     C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "unboundExample").get(0);
                     C_Parameter parameter = requestOrThrow(method, EXECUTABLE_GET_PARAMETERS).get(0);
                     C_Interface type = (C_Interface) requestOrThrow(parameter, VARIABLE_GET_TYPE);
                     C_Wildcard wildcard = ((C_Wildcard) requestOrThrow(type, INTERFACE_GET_GENERIC_TYPES).get(0));
                     Assertions.assertEquals("?", render(DEFAULT, wildcard).type());
                  });
   }
}