package com.derivandi.shadow.structure;

import com.derivandi.api.Ap;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.derivandi.api.test.ProcessorTest.processorTest;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

class ConstructorTest
{
   @Test
   void getEmptyParameters()
   {
      processorTest().withCodeToCompile("DefaultConstructorExample.java", "public class DefaultConstructorExample{}")
                     .process(context ->
                              {
                                 Ap.Class example = context.getClassOrThrow("DefaultConstructorExample");
                                 Ap.Constructor constructor = example.getConstructors().get(0);

                                 assertEquals(0, constructor.getParameters().size());
                              });
   }

   @Test
   void getParameters()
   {
      processorTest().withCodeToCompile("ConstructorExample.java", "public class ConstructorExample {public ConstructorExample(Long id) {}}")
                     .process(context ->
                              {
                                 Ap.Class example = context.getClassOrThrow("ConstructorExample");
                                 Ap.Class aLong = context.getClassOrThrow("java.lang.Long");
                                 Ap.Constructor constructor = example.getConstructors().get(0);
                                 List<Ap.Parameter> parameters = constructor.getParameters();

                                 assertEquals(1, parameters.size());
                                 assertEquals(aLong, parameters.get(0).getType());
                              });
   }

   @Test
   void getParameterTypes()
   {
      processorTest().withCodeToCompile("ConstructorExample.java", "public class ConstructorExample {public ConstructorExample(Long id) {}}")
                     .process(context ->
                              {
                                 Ap.Class aLong = context.getClassOrThrow("java.lang.Long");
                                 Ap.Class example = context.getClassOrThrow("ConstructorExample");
                                 Ap.Constructor constructor = example.getConstructors().get(0);

                                 assertEquals(List.of(aLong), constructor.getParameterTypes());
                              });
   }

   @Test
   void getThrows()
   {
      processorTest().withCodeToCompile("ConstructorExample.java", """
                                                                   import java.io.IOException;
                                                                   
                                                                   public class ConstructorExample {
                                                                      public ConstructorExample(Long id) {}
                                                                      public ConstructorExample(String name) throws IOException {}
                                                                   }
                                                                   """)
                     .process(context ->
                              {
                                 Ap.Class ioException = context.getClassOrThrow("java.io.IOException");
                                 Ap.Class example = context.getClassOrThrow("ConstructorExample");
                                 List<Ap.Constructor> constructors = example.getConstructors();

                                 assertEquals(emptyList(), constructors.get(0).getThrows());
                                 assertEquals(List.of(ioException), constructors.get(1).getThrows());
                              });
   }

   @Test
   void isVarArgs()
   {
      processorTest().withCodeToCompile("ConstructorExample.java", """
                                                                   import java.io.IOException;
                                                                   
                                                                   public class ConstructorExample {
                                                                      public ConstructorExample(Long id) {}
                                                                      public ConstructorExample(String... names) {}
                                                                   }
                                                                   """)
                     .process(context ->
                              {
                                 Ap.Class example = context.getClassOrThrow("ConstructorExample");
                                 List<Ap.Constructor> constructors = example.getConstructors();
                                 assertFalse(constructors.get(0).isVarArgs());
                                 assertTrue(constructors.get(1).isVarArgs());
                              });
   }

   @Test
   void getSurrounding()
   {
      processorTest().withCodeToCompile("ConstructorExample.java", """
                                                                   import java.io.IOException;
                                                                   
                                                                   public class ConstructorExample {
                                                                      public ConstructorExample(Long id) {}
                                                                      public ConstructorExample(String name) throws IOException {}
                                                                      public ConstructorExample(String... names) {}
                                                                   }
                                                                   """)
                     .process(context ->
                              {
                                 Ap.Class example = context.getClassOrThrow("ConstructorExample");
                                 Ap.Constructor constructor = example.getConstructors().get(0);
                                 assertEquals(example, constructor.getSurrounding());
                              });
   }

   @Test
   void getReceiverType()
   {
      processorTest().withCodeToCompile("DefaultConstructorExample.java", "public class DefaultConstructorExample{}")
                     .withCodeToCompile("ReceiverExample.java", """
                                                                public class ReceiverExample {
                                                                   public class Inner {
                                                                      public Inner(ReceiverExample ReceiverExample.this) {}
                                                                   }
                                                                }
                                                                """)
                     .process(context ->
                              {
                                 Ap.Class defaultConstructorExample = context.getClassOrThrow("DefaultConstructorExample");
                                 Ap.Constructor defaultConstructor = defaultConstructorExample.getConstructors().get(0);
                                 assertTrue(defaultConstructor.getReceiverType().isEmpty());

                                 Ap.Class example = context.getClassOrThrow("ReceiverExample");
                                 Ap.Class inner = context.getClassOrThrow("ReceiverExample.Inner");
                                 Ap.Constructor constructor = inner.getConstructors().get(0);
                                 assertEquals(example, constructor.getReceiverType().orElseThrow());
                              });
   }
}
