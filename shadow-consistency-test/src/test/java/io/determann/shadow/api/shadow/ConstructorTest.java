package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConstructorTest extends ExecutableTest<Constructor>
{
   @Test
   void testGetParameters()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(0,
                                            query(query(shadowApi.getClassOrThrow("DefaultConstructorExample"))
                                                  .getConstructors()
                                                  .get(0))
                                                  .getParameters()
                                                  .size());

                               List<Constructor> constructors = query(shadowApi.getClassOrThrow("ConstructorExample"))
                                                                         .getConstructors();
                               assertEquals(3, constructors.size());
                               assertEquals(shadowApi.getClassOrThrow("java.lang.Long"),
                                            query(query(constructors.get(0))
                                                        .getParameters()
                                                        .get(0))
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
      ProcessorTest.process(shadowApi -> assertEquals(shadowApi.getConstants().getVoid(),
                                                      query(query(shadowApi.getClassOrThrow("ConstructorExample"))
                                                            .getConstructors()
                                                            .get(0))
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
      ProcessorTest.process(shadowApi -> assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.Long")),
                                                      query(query(shadowApi.getClassOrThrow("ConstructorExample"))
                                                            .getConstructors()
                                                            .get(0))
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
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(List.of(),
                                            query(query(shadowApi.getClassOrThrow("ConstructorExample"))
                                                  .getConstructors()
                                                  .get(0))
                                                  .getThrows());

                               assertEquals(List.of(shadowApi.getClassOrThrow("java.io.IOException")),
                                            query(query(shadowApi.getClassOrThrow("ConstructorExample"))
                                                  .getConstructors()
                                                  .get(1))
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
      ProcessorTest.process(shadowApi -> assertTrue(query(query(shadowApi.getClassOrThrow("ConstructorExample"))
                                                          .getConstructors()
                                                          .get(2))
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
      ProcessorTest.process(shadowApi ->
                            {
                               Class aClass = shadowApi.getClassOrThrow("ConstructorExample");
                               assertEquals(aClass, query(query(aClass).getConstructors().get(0)).getSurrounding());
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
      ProcessorTest.process(shadowApi -> assertEquals(shadowApi.getPackages("io.determann.shadow.example.processed.test.constructor").get(0),
                                                      query(shadowApi.getClassOrThrow(
                                                                     "io.determann.shadow.example.processed.test.constructor.ConstructorExample"))
                                                               .getPackage()))
                   .withCodeToCompile("ConstructorExample.java", """
                         package io.determann.shadow.example.processed.test.constructor;
                                                    
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
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(Optional.empty(),
                                            query(query(shadowApi.getClassOrThrow("DefaultConstructorExample"))
                                                  .getConstructors()
                                                  .get(0))
                                                  .getReceiverType());

                               assertEquals(shadowApi.getClassOrThrow("ReceiverExample"),
                                            query(query(shadowApi.getClassOrThrow("ReceiverExample.Inner"))
                                                  .getConstructors()
                                                  .get(0))
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
