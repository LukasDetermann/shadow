package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.structure.MethodLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ClassLangModel;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static org.junit.jupiter.api.Assertions.*;

class MethodTest extends ExecutableTest<Method>
{
   @Test
   void testisSubSignatureOf()
   {
      ProcessorTest.process(context ->
                            {
                               List<MethodLangModel> methods = context.getClassOrThrow("SubSignature").getMethods();
                               MethodLangModel first = methods.get(0);
                               MethodLangModel second = methods.get(1);
                               MethodLangModel third = methods.get(2);
                               MethodLangModel four = methods.get(3);
                               MethodLangModel five = methods.get(4);
                               MethodLangModel six = methods.get(5);
                               MethodLangModel seven = methods.get(6);

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
      ConsistencyTest.<Class>compileTime(context -> context.getClassOrThrow("MethodExample"))
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

      ProcessorTest.process(context ->
                            {
                               assertTrue(context.getClassOrThrow("MethodExample")
                                                          .getMethods("toString").get(0)
                                                          .overrides(context.getClassOrThrow("java.lang.Object")
                                                                                     .getMethods("toString")
                                                                                     .get(0)));

                               assertFalse(context.getClassOrThrow("MethodExample")
                                                           .getMethods("toString").get(0)
                                                           .overrides(context.getClassOrThrow("java.lang.Object")
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
      ProcessorTest.process(context -> assertEquals("[args]",
                                                      context.getClassOrThrow("MethodExample")
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
      ProcessorTest.process(context ->
                                  assertEquals(List.of(context.asArray(context.getClassOrThrow("java.lang.String"))),
                                               context.getClassOrThrow("MethodExample")
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
      ProcessorTest.process(context ->
                            {
                               assertEquals(context.getClassOrThrow("java.lang.String"),
                                            context.getClassOrThrow("java.lang.String")
                                                            .getMethods("toString").get(0)
                                                            .getReturnType());

                               assertNotEquals(context.getClassOrThrow("java.lang.Long"),
                                               context.getClassOrThrow("java.lang.String")
                                                               .getMethods("toString").get(0)
                                                               .getReturnType());
                            })
                   .compile();
   }

   @Test
   @Override
   void testGetThrows()
   {
      ProcessorTest.process(context ->
                            {
                               assertEquals(List.of(),
                                            context.getClassOrThrow("java.lang.Object")
                                                            .getMethods("toString").get(0)
                                                            .getThrows());

                               assertEquals(List.of(context.getClassOrThrow("java.lang.InterruptedException")),
                                            context.getClassOrThrow("java.lang.Object")
                                                            .getMethods("wait").get(0)
                                                            .getThrows());
                            })
                   .compile();
   }

   @Test
   @Override
   void testIsVarArgs()
   {
      ProcessorTest.process(context ->
                            {
                               assertFalse(context.getClassOrThrow("java.lang.Object")
                                                           .getMethods("toString").get(0)
                                                           .isVarArgs());
                               assertTrue(context.getClassOrThrow("MethodExample")
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
      ProcessorTest.process(context ->
                            {
                               ClassLangModel aClass = context.getClassOrThrow("MethodExample");
                               assertEquals(aClass, aClass.getMethods("toString").get(0).getSurrounding());
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
      ProcessorTest.process(context -> assertEquals(context.getPackages("io.determann.shadow.example.processed.test.method").get(0),
                                                      context.getClassOrThrow("io.determann.shadow.example.processed.test.method.MethodExample")
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
      ProcessorTest.process(context ->
                            {
                               ClassLangModel aClass = context.getClassOrThrow("MethodExample");

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
