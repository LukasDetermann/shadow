package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.C;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class VariableTest
{
   @Test
   void isSubtypeOf()
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
                     C.Class string = requestOrThrow(implementation, GET_CLASS, "java.lang.String");
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "ParameterExample");
                     C.Constructor constructor = requestOrThrow(example, CLASS_GET_CONSTRUCTORS).get(0);
                     C.Parameter parameter = requestOrThrow(constructor, EXECUTABLE_GET_PARAMETERS).get(0);
                     assertTrue(requestOrThrow(parameter, VARIABLE_IS_SUBTYPE_OF, string));
                  });
   }

   @Test
   void isAssignableFrom()
   {
      withSource("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
            .test(implementation ->
                  {
                     C.Class integer = requestOrThrow(implementation, GET_CLASS, "java.lang.Integer");
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "FieldExample");
                     C.Field field = requestOrThrow(example, DECLARED_GET_FIELDS).get(0);
                     assertTrue(requestOrThrow(field, VARIABLE_IS_ASSIGNABLE_FROM, integer));
                  });
   }
}
