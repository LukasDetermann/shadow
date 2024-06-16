package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.LangModelQueries;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.consistency.ConsistencyTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static io.determann.shadow.meta_meta.Operations.*;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;
import static org.junit.jupiter.api.Assertions.*;

class MethodTest extends ExecutableTest<Method>
{
   @Test
   void testisSubSignatureOf()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               List<Method> methods = query(shadowApi.getClassOrThrow("SubSignature")).getMethods();
                               Method first = methods.get(0);
                               Method second = methods.get(1);
                               Method third = methods.get(2);
                               Method four = methods.get(3);
                               Method five = methods.get(4);
                               Method six = methods.get(5);
                               Method seven = methods.get(6);

                               assertTrue(query(first).sameParameterTypes(second));
                               assertTrue(query(second).sameParameterTypes(first));
                               assertFalse(query(third).sameParameterTypes(four));
                               assertFalse(query(four).sameParameterTypes(third));
                               assertTrue(query(four).sameParameterTypes(five));
                               assertTrue(query(six).sameParameterTypes(seven));
                               assertFalse(query(seven).sameParameterTypes(six));
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
      ConsistencyTest.compileTime(context -> context.getClassOrThrow("MethodExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("MethodExample")))
                     .withCode("MethodExample.java", """
                           public class MethodExample {
                              @Override
                              public String toString()
                              {
                                 return "MethodExample{}";
                              }
                           }
                           """)
                     .test(aClass -> assertTrue(requestOrThrow(requestOrThrow(aClass, DECLARED_GET_METHOD, "toString").get(0),
                                                               METHOD_OVERRIDES,
                                                               requestOrThrow(requestOrThrow(aClass, CLASS_GET_SUPER_CLASS), DECLARED_GET_METHOD, "toString").get(0))));

      ProcessorTest.process(shadowApi ->
                            {
                               assertTrue(LangModelQueries.query(LangModelQueries.query(shadowApi.getClassOrThrow("MethodExample"))
                                                          .getMethods("toString").get(0))
                                                          .overrides(LangModelQueries.query(shadowApi.getClassOrThrow("java.lang.Object"))
                                                                                     .getMethods("toString")
                                                                                     .get(0)));

                               assertFalse(LangModelQueries.query(LangModelQueries.query(shadowApi.getClassOrThrow("MethodExample"))
                                                           .getMethods("toString").get(0))
                                                           .overrides(LangModelQueries.query(shadowApi.getClassOrThrow("java.lang.Object"))
                                                                                      .getMethods("clone")
                                                                                      .get(0)));
                            })
                   .withCodeToCompile("MethodExample.java", """
                         public class MethodExample {
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
                                                      LangModelQueries.query(LangModelQueries.query(shadowApi.getClassOrThrow("MethodExample"))
                                                                      .getMethods("varArgsMethod").get(0)).getParameters().toString()))
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
                                               LangModelQueries.query(LangModelQueries.query(shadowApi.getClassOrThrow("MethodExample"))
                                                               .getMethods("varArgsMethod").get(0)).getParameterTypes()))
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
                                            LangModelQueries.query(LangModelQueries.query(shadowApi.getClassOrThrow("java.lang.String"))
                                                            .getMethods("toString").get(0))
                                                            .getReturnType());

                               assertNotEquals(shadowApi.getClassOrThrow("java.lang.Long"),
                                               LangModelQueries.query(LangModelQueries.query(shadowApi.getClassOrThrow("java.lang.String"))
                                                               .getMethods("toString").get(0))
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
                                            LangModelQueries.query(LangModelQueries.query(shadowApi.getClassOrThrow("java.lang.Object"))
                                                            .getMethods("toString").get(0))
                                                            .getThrows());

                               assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.InterruptedException")),
                                            LangModelQueries.query(LangModelQueries.query(shadowApi.getClassOrThrow("java.lang.Object"))
                                                            .getMethods("wait").get(0))
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
                               assertFalse(LangModelQueries.query(LangModelQueries.query(shadowApi.getClassOrThrow("java.lang.Object"))
                                                           .getMethods("toString").get(0))
                                                           .isVarArgs());
                               assertTrue(LangModelQueries.query(LangModelQueries.query(shadowApi.getClassOrThrow("MethodExample"))
                                                          .getMethods("varArgsMethod").get(0))
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
                               assertEquals(aClass, LangModelQueries.query(LangModelQueries.query(aClass).getMethods("toString").get(0))
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
                                                      LangModelQueries.query(LangModelQueries.query(shadowApi.getClassOrThrow("io.determann.shadow.example.processed.test.method.MethodExample"))
                                                                      .getMethods("toString")
                                                                      .get(0))
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

                               assertTrue(LangModelQueries.query(LangModelQueries.query(aClass).getMethods("toString").get(0)).getReceiverType().isEmpty());
                               assertEquals(aClass, LangModelQueries.query(LangModelQueries.query(aClass).getMethods("receiver").get(0)).getReceiverType().orElseThrow());
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
