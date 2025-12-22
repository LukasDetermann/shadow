package io.determann.shadow.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

class ConstructorTest
{
   @Test
   void getEmptyParameters()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class example = context.getClassOrThrow("DefaultConstructorExample");
                               Ap.Constructor constructor = example.getConstructors().get(0);

                               assertEquals(0, constructor.getParameters().size());
                            })
                   .withCodeToCompile("DefaultConstructorExample.java", "public class DefaultConstructorExample{}")
                   .compile();
   }


   @Test
   void getParameters()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class example = context.getClassOrThrow("ConstructorExample");
                               Ap.Class aLong = context.getClassOrThrow("java.lang.Long");
                               Ap.Constructor constructor = example.getConstructors().get(0);
                               List<Ap.Parameter> parameters = constructor.getParameters();

                               assertEquals(1, parameters.size());
                               assertEquals(aLong, parameters.get(0).getType());
                            })
                   .withCodeToCompile("ConstructorExample.java", "public class ConstructorExample {public ConstructorExample(Long id) {}}")
                   .compile();
   }

   @Test
   void getParameterTypes()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class aLong = context.getClassOrThrow("java.lang.Long");
                               Ap.Class example = context.getClassOrThrow("ConstructorExample");
                               Ap.Constructor constructor = example.getConstructors().get(0);

                               assertEquals(List.of(aLong), constructor.getParameterTypes());
                            })
                   .withCodeToCompile("ConstructorExample.java", "public class ConstructorExample {public ConstructorExample(Long id) {}}")
                   .compile();
   }

   @Test
   void getThrows()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class ioException = context.getClassOrThrow("java.io.IOException");
                               Ap.Class example = context.getClassOrThrow("ConstructorExample");
                               List<Ap.Constructor> constructors = example.getConstructors();

                               assertEquals(emptyList(), constructors.get(0).getThrows());
                               assertEquals(List.of(ioException), constructors.get(1).getThrows());
                            })
                   .withCodeToCompile("ConstructorExample.java", """
                                                                 import java.io.IOException;
                                                                 
                                                                 public class ConstructorExample {
                                                                    public ConstructorExample(Long id) {}
                                                                    public ConstructorExample(String name) throws IOException {}
                                                                 }
                                                                 """)
                   .compile();
   }

   @Test
   void isVarArgs()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class example = context.getClassOrThrow("ConstructorExample");
                               List<Ap.Constructor> constructors = example.getConstructors();
                               assertFalse(constructors.get(0).isVarArgs());
                               assertTrue(constructors.get(1).isVarArgs());
                            })
                   .withCodeToCompile("ConstructorExample.java", """
                                                                 import java.io.IOException;
                                                                 
                                                                 public class ConstructorExample {
                                                                    public ConstructorExample(Long id) {}
                                                                    public ConstructorExample(String... names) {}
                                                                 }
                                                                 """)
                   .compile();
   }

   @Test
   void getSurrounding()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class example = context.getClassOrThrow("ConstructorExample");
                               Ap.Constructor constructor = example.getConstructors().get(0);
                               assertEquals(example, constructor.getSurrounding());
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
   void getReceiverType()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class defaultConstructorExample = context.getClassOrThrow("DefaultConstructorExample");
                               Ap.Constructor defaultConstructor = defaultConstructorExample.getConstructors().get(0);
                               assertTrue(defaultConstructor.getReceiverType().isEmpty());

                               Ap.Class example = context.getClassOrThrow("ReceiverExample");
                               Ap.Class inner = context.getClassOrThrow("ReceiverExample.Inner");
                               Ap.Constructor constructor = inner.getConstructors().get(0);
                               assertEquals(example, constructor.getReceiverType().orElseThrow());
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
