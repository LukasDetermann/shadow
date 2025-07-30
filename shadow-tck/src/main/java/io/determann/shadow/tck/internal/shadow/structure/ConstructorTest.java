package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.C;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
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
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "DefaultConstructorExample");
                     C.Constructor constructor = requestOrThrow(example, CLASS_GET_CONSTRUCTORS).get(0);

                     assertEquals(0, requestOrThrow(constructor, EXECUTABLE_GET_PARAMETERS).size());
                  });
   }


   @Test
   void getParameters()
   {
      withSource("ConstructorExample.java", "public class ConstructorExample {public ConstructorExample(Long id) {}}")
            .test(implementation ->
                  {
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     C.Class aLong = requestOrThrow(implementation, GET_CLASS, "java.lang.Long");
                     C.Constructor constructor = requestOrThrow(example, CLASS_GET_CONSTRUCTORS).get(0);
                     List<? extends C.Parameter> parameters = requestOrThrow(constructor, EXECUTABLE_GET_PARAMETERS);

                     assertEquals(1, parameters.size());
                     assertEquals(aLong, requestOrThrow(parameters.get(0), VARIABLE_GET_TYPE));
                  });
   }

   @Test
   void getParameterTypes()
   {
      withSource("ConstructorExample.java", "public class ConstructorExample {public ConstructorExample(Long id) {}}")
            .test(implementation ->
                  {
                     C.Class aLong = requestOrThrow(implementation, GET_CLASS, "java.lang.Long");
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     C.Constructor constructor = requestOrThrow(example, CLASS_GET_CONSTRUCTORS).get(0);

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
                     C.Class ioException = requestOrThrow(implementation, GET_CLASS, "java.io.IOException");
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     List<? extends C.Constructor> constructors = requestOrThrow(example, CLASS_GET_CONSTRUCTORS);

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
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     List<? extends C.Constructor> constructors = requestOrThrow(example, CLASS_GET_CONSTRUCTORS);
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
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     C.Constructor constructor = requestOrThrow(example, CLASS_GET_CONSTRUCTORS).get(0);
                     assertEquals(example, requestOrThrow(constructor, EXECUTABLE_GET_SURROUNDING));
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
                     C.Class defaultConstructorExample = requestOrThrow(implementation, GET_CLASS, "DefaultConstructorExample");
                     C.Constructor defaultConstructor = requestOrThrow(defaultConstructorExample, CLASS_GET_CONSTRUCTORS).get(0);
                     assertTrue(requestOrEmpty(defaultConstructor, EXECUTABLE_GET_RECEIVER_TYPE).isEmpty());

                     C.Class example = requestOrThrow(implementation, GET_CLASS, "ReceiverExample");
                     C.Class inner = requestOrThrow(implementation, GET_CLASS, "ReceiverExample.Inner");
                     C.Constructor constructor = requestOrThrow(inner, CLASS_GET_CONSTRUCTORS).get(0);
                     assertEquals(example, requestOrThrow(constructor, EXECUTABLE_GET_RECEIVER_TYPE));
                  });
   }
}
