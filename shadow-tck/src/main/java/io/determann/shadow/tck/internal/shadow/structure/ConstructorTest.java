package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.shadow.structure.C_Constructor;
import io.determann.shadow.api.shadow.structure.C_Package;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.type.C_Class;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

class ConstructorTest
{
   @Test
   void getEmptyParameters()
   {
      withSource("DefaultConstructorExample.java", "public class DefaultConstructorExample{}")
            .test(implementation ->
                  {
                     C_Class example = requestOrThrow(implementation, GET_CLASS, "DefaultConstructorExample");
                     C_Constructor constructor = requestOrThrow(example, CLASS_GET_CONSTRUCTORS).get(0);

                     assertEquals(0, requestOrThrow(constructor, EXECUTABLE_GET_PARAMETERS).size());
                  });
   }


   @Test
   void getParameters()
   {
      withSource("ConstructorExample.java", "public class ConstructorExample {public ConstructorExample(Long id) {}}")
            .test(implementation ->
                  {
                     C_Class example = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     C_Class aLong = requestOrThrow(implementation, GET_CLASS, "java.lang.Long");
                     C_Constructor constructor = requestOrThrow(example, CLASS_GET_CONSTRUCTORS).get(0);
                     List<? extends C_Parameter> parameters = requestOrThrow(constructor, EXECUTABLE_GET_PARAMETERS);

                     assertEquals(1, parameters.size());
                     assertEquals(aLong, requestOrThrow(parameters.get(0), VARIABLE_GET_TYPE));
                  });
   }

   @Test
   void getReturnType()
   {
      withSource("ConstructorExample.java", """
            import java.io.IOException;
            
            public class ConstructorExample {
               public ConstructorExample(Long id) {}
            }
            """)
            .test(implementation ->
                  {
                     C_Class example = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     C_Constructor constructor = requestOrThrow(example, CLASS_GET_CONSTRUCTORS).get(0);
                     assertEquals(requestOrThrow(implementation, GET_VOID), requestOrThrow(constructor, EXECUTABLE_GET_RETURN_TYPE));
                  });
   }

   @Test
   void getParameterTypes()
   {
      withSource("ConstructorExample.java", "public class ConstructorExample {public ConstructorExample(Long id) {}}")
            .test(implementation ->
                  {
                     C_Class aLong = requestOrThrow(implementation, GET_CLASS, "java.lang.Long");
                     C_Class example = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     C_Constructor constructor = requestOrThrow(example, CLASS_GET_CONSTRUCTORS).get(0);

                     assertEquals(List.of(aLong), requestOrThrow(constructor, EXECUTABLE_GET_PARAMETER_TYPES));
                  });
   }

   @Test
   void getThrows()
   {
      withSource("ConstructorExample.java", """
            import java.io.IOException;
            
            public class ConstructorExample {
               public ConstructorExample(Long id) {}
               public ConstructorExample(String name) throws IOException {}
            }
            """)
            .test(implementation ->
                  {
                     C_Class ioException = requestOrThrow(implementation, GET_CLASS, "java.io.IOException");
                     C_Class example = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     List<? extends C_Constructor> constructors = requestOrThrow(example, CLASS_GET_CONSTRUCTORS);

                     assertEquals(emptyList(), requestOrThrow(constructors.get(0), EXECUTABLE_GET_THROWS));
                     assertEquals(List.of(ioException), requestOrThrow(constructors.get(1), EXECUTABLE_GET_THROWS));
                  });
   }

   @Test
   void isVarArgs()
   {
      withSource("ConstructorExample.java", """
            import java.io.IOException;
            
            public class ConstructorExample {
               public ConstructorExample(Long id) {}
               public ConstructorExample(String... names) {}
            }
            """)
            .test(implementation ->
                  {
                     C_Class example = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     List<? extends C_Constructor> constructors = requestOrThrow(example, CLASS_GET_CONSTRUCTORS);
                     assertFalse(requestOrThrow(constructors.get(0), EXECUTABLE_IS_VAR_ARGS));
                     assertTrue(requestOrThrow(constructors.get(1), EXECUTABLE_IS_VAR_ARGS));
                  });
   }

   @Test
   void getSurrounding()
   {
      withSource("ConstructorExample.java", """
            import java.io.IOException;
            
            public class ConstructorExample {
               public ConstructorExample(Long id) {}
               public ConstructorExample(String name) throws IOException {}
               public ConstructorExample(String... names) {}
            }
            """)
            .test(implementation ->
                  {
                     C_Class example = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     C_Constructor constructor = requestOrThrow(example, CLASS_GET_CONSTRUCTORS).get(0);
                     assertEquals(example, requestOrThrow(constructor, EXECUTABLE_GET_SURROUNDING));
                  });
   }

   @Test
   void getPackage()
   {
      withSource("ConstructorExample.java", """
            package io.determann.shadow.example.processed.test.constructor;
            
            public class ConstructorExample {
               public ConstructorExample(Long id) {}
            }
            """)
            .test(implementation ->
                  {
                     C_Package aPackage = requestOrThrow(implementation,
                                                         GET_PACKAGES,
                                                         "io.determann.shadow.example.processed.test.constructor").get(0);

                     C_Class example = requestOrThrow(implementation,
                                                      GET_CLASS,
                                                      "io.determann.shadow.example.processed.test.constructor.ConstructorExample");

                     C_Constructor constructor = requestOrThrow(example, CLASS_GET_CONSTRUCTORS).get(0);
                     assertEquals(aPackage, requestOrThrow(constructor, EXECUTABLE_GET_PACKAGE));
                  });
   }

   @Test
   void getReceiverType()
   {
      withSource("DefaultConstructorExample.java", "public class DefaultConstructorExample{}")
            .withSource("ReceiverExample.java", """
                  public class ReceiverExample {
                     public class Inner {
                        public Inner(ReceiverExample ReceiverExample.this) {}
                     }
                  }
                  """)
            .test(implementation ->
                  {
                     C_Class defaultConstructorExample = requestOrThrow(implementation, GET_CLASS, "DefaultConstructorExample");
                     C_Constructor defaultConstructor = requestOrThrow(defaultConstructorExample, CLASS_GET_CONSTRUCTORS).get(0);
                     assertTrue(requestOrEmpty(defaultConstructor, EXECUTABLE_GET_RECEIVER_TYPE).isEmpty());

                     C_Class example = requestOrThrow(implementation, GET_CLASS, "ReceiverExample");
                     C_Class inner = requestOrThrow(implementation, GET_CLASS, "ReceiverExample.Inner");
                     C_Constructor constructor = requestOrThrow(inner, CLASS_GET_CONSTRUCTORS).get(0);
                     assertEquals(example, requestOrThrow(constructor, EXECUTABLE_GET_RECEIVER_TYPE));
                  });
   }
}
