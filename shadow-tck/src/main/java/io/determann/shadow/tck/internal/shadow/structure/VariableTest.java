package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.shadow.structure.C_Constructor;
import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.type.C_Class;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
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
                     C_Class string = requestOrThrow(implementation, GET_CLASS, "java.lang.String");
                     C_Class example = requestOrThrow(implementation, GET_CLASS, "ParameterExample");
                     C_Constructor constructor = requestOrThrow(example, CLASS_GET_CONSTRUCTORS).get(0);
                     C_Parameter parameter = requestOrThrow(constructor, EXECUTABLE_GET_PARAMETERS).get(0);
                     assertTrue(requestOrThrow(parameter, VARIABLE_IS_SUBTYPE_OF, string));
                  });
   }

   @Test
   void isAssignableFrom()
   {
      withSource("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
            .test(implementation ->
                  {
                     C_Class integer = requestOrThrow(implementation, GET_CLASS, "java.lang.Integer");
                     C_Class example = requestOrThrow(implementation, GET_CLASS, "FieldExample");
                     C_Field field = requestOrThrow(example, DECLARED_GET_FIELDS).get(0);
                     assertTrue(requestOrThrow(field, VARIABLE_IS_ASSIGNABLE_FROM, integer));
                  });
   }
}
