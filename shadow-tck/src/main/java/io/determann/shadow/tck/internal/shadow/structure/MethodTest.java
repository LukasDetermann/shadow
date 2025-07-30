package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.C;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.test;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

class MethodTest
{
   @Test
   void isSubSignatureOf()
   {
      withSource("SubSignature.java", """
            import java.util.List;
            
            public abstract class SubSignature {
               public abstract void first();
               public abstract void second();
               public abstract void third(String name, Long id);
               public abstract void four(Long id, String name);
               public abstract void five(Long id, String name2);
               public abstract void six(List list);
               public abstract void seven(List<String> strings);
            }
            """)
            .test(implementation ->
                  {
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "SubSignature");
                     List<? extends C.Method> methods = requestOrThrow(example, DECLARED_GET_METHODS);
                     C.Method first = methods.get(0);
                     C.Method second = methods.get(1);
                     C.Method third = methods.get(2);
                     C.Method four = methods.get(3);
                     C.Method five = methods.get(4);
                     C.Method six = methods.get(5);
                     C.Method seven = methods.get(6);

                     assertTrue(requestOrThrow(first, METHOD_SAME_PARAMETER_TYPES, second));
                     assertTrue(requestOrThrow(second, METHOD_SAME_PARAMETER_TYPES, first));
                     Assertions.assertFalse(requestOrThrow(third, METHOD_SAME_PARAMETER_TYPES, four));
                     Assertions.assertFalse(requestOrThrow(four, METHOD_SAME_PARAMETER_TYPES, third));
                     assertTrue(requestOrThrow(four, METHOD_SAME_PARAMETER_TYPES, five));
                     assertTrue(requestOrThrow(six, METHOD_SAME_PARAMETER_TYPES, seven));
                     Assertions.assertFalse(requestOrThrow(seven, METHOD_SAME_PARAMETER_TYPES, six));
                  });
   }

   @Test
   void overrides()
   {
      withSource("MethodExample.java", """
            public class MethodExample {
               @Override
               public String toString()
               {
                  return "MethodExample{}";
               }
            }
            """)
            .test(implementation ->
                  {
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "MethodExample");
                     C.Class superClass = requestOrThrow(example, CLASS_GET_SUPER_CLASS);
                     C.Method method = requestOrThrow(example, DECLARED_GET_METHOD, "toString").get(0);
                     C.Method superMethod = requestOrThrow(superClass, DECLARED_GET_METHOD, "toString").get(0);
                     C.Method otherMethod = requestOrThrow(superClass, DECLARED_GET_METHOD, "clone").get(0);

                     assertTrue(requestOrThrow(method, METHOD_OVERRIDES, superMethod));
                     assertFalse(requestOrThrow(method, METHOD_OVERRIDES, otherMethod));
                  });
   }

   @Test
   void getParameters()
   {
      withSource("MethodExample.java", "public class MethodExample {private void varArgsMethod(String... args) {}}")
            .test(implementation ->
                  {
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "MethodExample");
                     C.Method method = requestOrThrow(example, DECLARED_GET_METHOD, "varArgsMethod").get(0);
                     String parameters = requestOrThrow(method, EXECUTABLE_GET_PARAMETERS)
                           .stream()
                           .map(parameter -> requestOrThrow(parameter, NAMEABLE_GET_NAME))
                           .collect(Collectors.joining());

                     assertEquals("args", parameters);
                  });
   }

   @Test
   void getParameterTypes()
   {
      withSource("MethodExample.java", "public class MethodExample {private void varArgsMethod(String... args) {}}")
            .test(implementation ->
                  {
                     C.Class string = requestOrThrow(implementation, GET_CLASS, "java.lang.String");
                     C.Array stringArray = requestOrThrow(string, DECLARED_AS_ARRAY);
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "MethodExample");
                     C.Method method = requestOrThrow(example, DECLARED_GET_METHOD, "varArgsMethod").get(0);
                     assertEquals(List.of(stringArray), requestOrThrow(method, EXECUTABLE_GET_PARAMETER_TYPES));
                  });
   }

   @Test
   void getReturnType()
   {
      test(implementation ->
           {
              C.Class string = requestOrThrow(implementation, GET_CLASS, "java.lang.String");
              C.Class aLong = requestOrThrow(implementation, GET_CLASS, "java.lang.Long");
              C.Method toString = requestOrThrow(string, DECLARED_GET_METHOD, "toString").get(0);
              C.Type returnType = requestOrThrow(toString, METHOD_GET_RETURN_TYPE);
              assertEquals(string, returnType);
              assertNotEquals(aLong, returnType);
           });
   }

   @Test
   void getThrows()
   {
      test(implementation ->
           {
              C.Class object = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");
              C.Class interruptedException = requestOrThrow(implementation, GET_CLASS, "java.lang.InterruptedException");
              C.Method toString = requestOrThrow(object, DECLARED_GET_METHOD, "toString").get(0);
              C.Method wait = requestOrThrow(object, DECLARED_GET_METHOD, "wait").get(0);

              assertEquals(emptyList(), requestOrThrow(toString, EXECUTABLE_GET_THROWS));
              assertEquals(List.of(interruptedException), requestOrThrow(wait, EXECUTABLE_GET_THROWS));
           });
   }

   @Test
   void isNotVarArgs()
   {
      test(implementation ->
           {
              C.Class object = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");
              C.Method toString = requestOrThrow(object, DECLARED_GET_METHOD, "toString").get(0);
              assertFalse(requestOrThrow(toString, EXECUTABLE_IS_VAR_ARGS));
           });
   }

   @Test
   void isVarArgs()
   {
      withSource("MethodExample.java", "public class MethodExample {private void varArgsMethod(String... args) {}}")
            .test(implementation ->
                  {
                     C.Class object = requestOrThrow(implementation, GET_CLASS, "MethodExample");
                     C.Method method = requestOrThrow(object, DECLARED_GET_METHOD, "varArgsMethod").get(0);
                     assertTrue(requestOrThrow(method, EXECUTABLE_IS_VAR_ARGS));
                  });
   }

   @Test
   void getSurrounding()
   {
      withSource("MethodExample.java", """
            public class MethodExample {
               @Override
               public String toString()
               {
                  return "MethodExample{}";
               }
            }
            """)
            .test(implementation ->
                  {
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "MethodExample");
                     C.Method toString = requestOrThrow(example, DECLARED_GET_METHOD, "toString").get(0);
                     assertEquals(example, requestOrThrow(toString, EXECUTABLE_GET_SURROUNDING));
                  });
   }

   @Test
   void getReceiverType()
   {
      withSource("MethodExample.java", """
            public class MethodExample {
               private void varArgsMethod(String... args) {}
            
               private void receiver(MethodExample MethodExample.this) {}
            
               @Override
               public String toString()
               {
                  return "MethodExample{}";
               }
            }
            """)
            .test(implementation ->
                  {
                     C.Class example = requestOrThrow(implementation, GET_CLASS, "MethodExample");
                     C.Method toString = requestOrThrow(example, DECLARED_GET_METHOD, "toString").get(0);
                     assertTrue(requestOrEmpty(toString, EXECUTABLE_GET_RECEIVER_TYPE).isEmpty());

                     C.Method method = requestOrThrow(example, DECLARED_GET_METHOD, "receiver").get(0);
                     assertEquals(example, requestOrThrow(method, EXECUTABLE_GET_RECEIVER_TYPE));
                  });
   }
}
