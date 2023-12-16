package io.determann.shadow.api.shadow;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MethodTest extends ExecutableTest<Method>
{
   MethodTest()
   {
      super(shadowApi -> shadowApi.getClassOrThrow("java.lang.String").getMethods("toString").get(0));
   }

   @Test
   void testisSubSignatureOf()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               List<Method> methods = shadowApi.getClassOrThrow("SubSignature").getMethods();
                               Method first = methods.get(0);
                               Method second = methods.get(1);
                               Method third = methods.get(2);
                               Method four = methods.get(3);
                               Method five = methods.get(4);
                               Method six = methods.get(5);
                               Method seven = methods.get(6);

                               assertTrue(first.sameParameterTypes(second));
                               assertTrue(second.sameParameterTypes(first));
                               assertFalse(third.sameParameterTypes(four));
                               assertFalse(four.sameParameterTypes(third));
                               assertTrue(four.sameParameterTypes(five));
                               assertTrue(six.sameParameterTypes(seven));
                               assertFalse(seven.sameParameterTypes(six));
                            })
                   .withCodeToCompile("SubSignature.java", """
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
                   .compile();
   }

   @Test
   void testOverrides()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertTrue(shadowApi.getClassOrThrow("MethodExample")
                                                   .getMethods("toString").get(0)
                                                   .overrides(shadowApi.getClassOrThrow("java.lang.Object").getMethods("toString").get(0)));

                               assertFalse(shadowApi.getClassOrThrow("MethodExample")
                                                    .getMethods("toString").get(0)
                                                    .overrides(shadowApi.getClassOrThrow("java.lang.Object").getMethods("clone").get(0)));
                            })
                   .withCodeToCompile("MethodExample.java", """
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
                   .compile();
   }

   @Test
   @Override
   void testGetParameters()
   {
      ProcessorTest.process(shadowApi -> assertEquals("[args]",
                                                      shadowApi.getClassOrThrow("MethodExample")
                                                               .getMethods("varArgsMethod").get(0).getParameters().toString()))
                   .withCodeToCompile("MethodExample.java", """
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
                   .compile();
   }

   @Test
   @Override
   void testGetParameterTypes()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals(List.of(shadowApi.asArray(shadowApi.getClassOrThrow("java.lang.String"))),
                                               shadowApi.getClassOrThrow("MethodExample")
                                                        .getMethods("varArgsMethod").get(0).getParameterTypes()))
                   .withCodeToCompile("MethodExample.java", """
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
                   .compile();
   }

   @Test
   @Override
   void testGetReturnType()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(shadowApi.getClassOrThrow("java.lang.String"),
                                            shadowApi.getClassOrThrow("java.lang.String")
                                                     .getMethods("toString").get(0)
                                                     .getReturnType());

                               assertNotEquals(shadowApi.getClassOrThrow("java.lang.Long"),
                                               shadowApi.getClassOrThrow("java.lang.String")
                                                        .getMethods("toString").get(0)
                                                        .getReturnType());
                            })
                   .compile();
   }

   @Test
   @Override
   void testGetThrows()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(List.of(),
                                            shadowApi.getClassOrThrow("java.lang.Object")
                                                     .getMethods("toString").get(0)
                                                     .getThrows());

                               assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.InterruptedException")),
                                            shadowApi.getClassOrThrow("java.lang.Object")
                                                     .getMethods("wait").get(0)
                                                     .getThrows());
                            })
                   .compile();
   }

   @Test
   @Override
   void testIsVarArgs()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertFalse(shadowApi.getClassOrThrow("java.lang.Object")
                                                    .getMethods("toString").get(0)
                                                    .isVarArgs());
                               assertTrue(shadowApi.getClassOrThrow("MethodExample")
                                                   .getMethods("varArgsMethod").get(0)
                                                   .isVarArgs());
                            })
                   .withCodeToCompile("MethodExample.java", """
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
                   .compile();
   }

   @Test
   @Override
   void testGetSurrounding()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Class aClass = shadowApi.getClassOrThrow("MethodExample");
                               assertEquals(aClass, aClass.getMethods("toString").get(0)
                                                          .getSurrounding());
                            })
                   .withCodeToCompile("MethodExample.java", """
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
                   .compile();
   }

   @Test
   @Override
   void testGetPackage()
   {
      ProcessorTest.process(shadowApi -> assertEquals(shadowApi.getPackages("io.determann.shadow.example.processed.test.method").get(0),
                                                      shadowApi.getClassOrThrow("io.determann.shadow.example.processed.test.method.MethodExample")
                                                               .getMethods("toString")
                                                               .get(0)
                                                               .getPackage()))
                   .withCodeToCompile("MethodExample.java", """
                         package io.determann.shadow.example.processed.test.method;
                                                    
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
                   .compile();
   }

   @Test
   @Override
   void testGetReceiverType()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Class aClass = shadowApi.getClassOrThrow("MethodExample");

                               assertTrue(aClass.getMethods("toString").get(0).getReceiverType().isEmpty());
                               assertEquals(aClass, aClass.getMethods("receiver").get(0).getReceiverType().orElseThrow());
                            })
                   .withCodeToCompile("MethodExample.java", """
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
                   .compile();
   }
}
