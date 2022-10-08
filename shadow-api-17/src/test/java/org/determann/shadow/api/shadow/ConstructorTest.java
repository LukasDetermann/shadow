package org.determann.shadow.api.shadow;

import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConstructorTest extends ExecutableTest<Constructor>
{
   ConstructorTest()
   {
      super(shadowApi -> shadowApi.getClass("DefaultConstructorExample")
                                  .getConstructors()
                                  .get(0));
   }

   @Test
   void testGetParameters()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(0,
                                              shadowApi.getClass("DefaultConstructorExample")
                                                       .getConstructors()
                                                       .get(0)
                                                       .getParameters()
                                                       .size());

                                 List<Constructor> constructors = shadowApi.getClass("ConstructorExample")
                                                                           .getConstructors();
                                 assertEquals(3, constructors.size());
                                 assertEquals(shadowApi.getClass("java.lang.Long"),
                                              constructors.get(0)
                                                          .getParameters()
                                                          .get(0)
                                                          .getType());
                              })
                     .withCodeToCompile("DefaultConstructorExample.java", "public class DefaultConstructorExample{}")
                     .withCodeToCompile("ConstructorExample.java", """
                           import java.io.IOException;

                           public class ConstructorExample {
                              public ConstructorExample(Long id) {}
                              public ConstructorExample(String name) throws IOException {}
                              public ConstructorExample(String... names) {}
                           }
                           """)
                     .compile();
   }

   @Test
   @Override
   void testGetReturnType()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getConstants().getVoid(),
                                                        shadowApi.getClass("ConstructorExample")
                                                                 .getConstructors()
                                                                 .get(0)
                                                                 .getReturnType()))
                     .withCodeToCompile("ConstructorExample.java", """
                           import java.io.IOException;

                           public class ConstructorExample {
                              public ConstructorExample(Long id) {}
                              public ConstructorExample(String name) throws IOException {}
                              public ConstructorExample(String... names) {}
                           }
                           """)
                     .compile();
   }

   @Test
   @Override
   void testGetParameterTypes()
   {
      CompilationTest.process(shadowApi -> assertEquals(List.of(shadowApi.getClass("java.lang.Long")),
                                                        shadowApi.getClass("ConstructorExample")
                                                                 .getConstructors()
                                                                 .get(0)
                                                                 .getParameterTypes()))
                     .withCodeToCompile("ConstructorExample.java", """
                           import java.io.IOException;

                           public class ConstructorExample {
                              public ConstructorExample(Long id) {}
                              public ConstructorExample(String name) throws IOException {}
                              public ConstructorExample(String... names) {}
                           }
                           """)
                     .compile();
   }

   @Test
   @Override
   void testGetThrows()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(List.of(),
                                              shadowApi.getClass("ConstructorExample")
                                                       .getConstructors()
                                                       .get(0)
                                                       .getThrows());

                                 assertEquals(List.of(shadowApi.getClass("java.io.IOException")),
                                              shadowApi.getClass("ConstructorExample")
                                                       .getConstructors()
                                                       .get(1)
                                                       .getThrows());
                              })
                     .withCodeToCompile("ConstructorExample.java", """
                           import java.io.IOException;

                           public class ConstructorExample {
                              public ConstructorExample(Long id) {}
                              public ConstructorExample(String name) throws IOException {}
                              public ConstructorExample(String... names) {}
                           }
                           """)
                     .compile();
   }

   @Test
   @Override
   void testIsVarArgs()
   {
      CompilationTest.process(shadowApi -> assertTrue(shadowApi.getClass(
                                                                     "ConstructorExample")
                                                               .getConstructors()
                                                               .get(2)
                                                               .isVarArgs()))
                     .withCodeToCompile("ConstructorExample.java", """
                           import java.io.IOException;

                           public class ConstructorExample {
                              public ConstructorExample(Long id) {}
                              public ConstructorExample(String name) throws IOException {}
                              public ConstructorExample(String... names) {}
                           }
                           """)
                     .compile();
   }

   @Test
   @Override
   void testGetSurrounding()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 Class aClass = shadowApi.getClass("ConstructorExample");
                                 assertEquals(aClass, aClass.getConstructors().get(0).getSurrounding());
                              })
                     .withCodeToCompile("ConstructorExample.java", """
                           import java.io.IOException;

                           public class ConstructorExample {
                              public ConstructorExample(Long id) {}
                              public ConstructorExample(String name) throws IOException {}
                              public ConstructorExample(String... names) {}
                           }
                           """)
                     .compile();
   }

   @Test
   @Override
   void testGetPackage()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getPackages("org.determann.shadow.example.processed.test.constructor").get(0),
                                                        shadowApi.getClass(
                                                                       "org.determann.shadow.example.processed.test.constructor.ConstructorExample")
                                                                 .getPackage()))
                     .withCodeToCompile("ConstructorExample.java", """
                           package org.determann.shadow.example.processed.test.constructor;
                                                      
                           import java.io.IOException;

                           public class ConstructorExample {
                              public ConstructorExample(Long id) {}
                              public ConstructorExample(String name) throws IOException {}
                              public ConstructorExample(String... names) {}
                           }
                           """)
                     .compile();
   }

   @Test
   void testGetReceiverType()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(Optional.empty(),
                                              shadowApi.getClass("DefaultConstructorExample")
                                                       .getConstructors()
                                                       .get(0)
                                                       .getReceiverType());

                                 assertEquals(shadowApi.getClass("ReceiverExample"),
                                              shadowApi.getClass("ReceiverExample.Inner")
                                                       .getConstructors()
                                                       .get(0)
                                                       .getReceiverType()
                                                       .orElseThrow());
                              })
                     .withCodeToCompile("DefaultConstructorExample.java", "public class DefaultConstructorExample{}")
                     .withCodeToCompile("ReceiverExample.java", """
                           public class ReceiverExample {
                              public class Inner {
                                 public Inner(ReceiverExample ReceiverExample.this) {}
                              }
                           }
                           """)
                     .compile();
   }
}
