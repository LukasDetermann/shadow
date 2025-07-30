package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.C;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.*;

class ParameterTest
{
   @Test
   void getSurrounding()
   {
      withSource("ParameterExample.java", """
            public class ParameterExample
            {
               public ParameterExample(String name) {}
            
               public void foo(Long foo) { }
            }
            """)
            .test(implementation ->
                  {
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "ParameterExample");
                     C.Method method = requestOrThrow(example, DECLARED_GET_METHOD, "foo").get(0);
                     C.Parameter foo = requestOrThrow(method, EXECUTABLE_GET_PARAMETER, "foo");
                     assertEquals(method, requestOrThrow(foo, PARAMETER_GET_SURROUNDING));

                     C.Constructor constructor = requestOrThrow(example, CLASS_GET_CONSTRUCTORS).get(0);
                     C.Parameter name = requestOrThrow(constructor, EXECUTABLE_GET_PARAMETERS).get(0);
                     assertEquals(constructor, requestOrThrow(name, PARAMETER_GET_SURROUNDING));
                  });
   }

   @Test
   void isVarArgs()
   {
      withSource("VarArgsExample.java",
                 """
                       class VarArgsExample{
                           VarArgsExample(String s, String... s1) {}
                       }
                       """)
            .test(implementation ->
                  {
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "VarArgsExample");
                     C.Constructor constructor = requestOrThrow(example, CLASS_GET_CONSTRUCTORS).get(0);
                     List<? extends C.Parameter> parameters = requestOrThrow(constructor, EXECUTABLE_GET_PARAMETERS);
                     assertFalse(requestOrThrow(parameters.get(0), PARAMETER_IS_VAR_ARGS));
                     assertTrue(requestOrThrow(parameters.get(1), PARAMETER_IS_VAR_ARGS));
                  });
   }
}
