package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Constructor;
import io.determann.shadow.api.lang_model.shadow.type.LM_Class;
import io.determann.shadow.api.shadow.structure.C_Constructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConstructorTest extends ExecutableTest<C_Constructor>
{
   @Test
   void testGetParameters()
   {
      ProcessorTest.process(context ->
                            {
                               assertEquals(0,
                                            context.getClassOrThrow("DefaultConstructorExample")
                                                  .getConstructors()
                                                  .get(0)
                                                  .getParameters()
                                                  .size());

                               List<LM_Constructor> constructors = context.getClassOrThrow("ConstructorExample")
                                                                          .getConstructors();
                               assertEquals(3, constructors.size());
                               assertEquals(context.getClassOrThrow("java.lang.Long"),
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
      ProcessorTest.process(context -> assertEquals(context.getConstants().getVoid(),
                                                      context.getClassOrThrow("ConstructorExample")
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
      ProcessorTest.process(context -> assertEquals(List.of(context.getClassOrThrow("java.lang.Long")),
                                                      context.getClassOrThrow("ConstructorExample")
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
      ProcessorTest.process(context ->
                            {
                               assertEquals(List.of(),
                                            context.getClassOrThrow("ConstructorExample")
                                                  .getConstructors()
                                                  .get(0)
                                                  .getThrows());

                               assertEquals(List.of(context.getClassOrThrow("java.io.IOException")),
                                            context.getClassOrThrow("ConstructorExample")
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
      ProcessorTest.process(context -> assertTrue(context.getClassOrThrow("ConstructorExample")
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
      ProcessorTest.process(context ->
                            {
                               LM_Class aClass = context.getClassOrThrow("ConstructorExample");
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
      ProcessorTest.process(context -> assertEquals(context.getPackages("io.determann.shadow.example.processed.test.constructor").get(0),
                                                      context.getClassOrThrow(
                                                                     "io.determann.shadow.example.processed.test.constructor.ConstructorExample")
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
      ProcessorTest.process(context ->
                            {
                               assertEquals(Optional.empty(),
                                            context.getClassOrThrow("DefaultConstructorExample")
                                                  .getConstructors()
                                                  .get(0)
                                                  .getReceiverType());

                               assertEquals(context.getClassOrThrow("ReceiverExample"),
                                            context.getClassOrThrow("ReceiverExample.Inner")
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
