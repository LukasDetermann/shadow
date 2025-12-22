package io.determann.shadow.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.*;

class MethodTest
{
   @Test
   void isSubSignatureOf()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class example = context.getClassOrThrow("SubSignature");
                               List<Ap.Method> methods = example.getMethods();
                               Ap.Method first = methods.get(0);
                               Ap.Method second = methods.get(1);
                               Ap.Method third = methods.get(2);
                               Ap.Method four = methods.get(3);
                               Ap.Method five = methods.get(4);
                               Ap.Method six = methods.get(5);
                               Ap.Method seven = methods.get(6);

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
   void overrides()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class example = context.getClassOrThrow("MethodExample");
                               Ap.Class superClass = example.getSuperClass();
                               Ap.Method method = example.getMethods("toString").get(0);
                               Ap.Method superMethod = superClass.getMethods("toString").get(0);
                               Ap.Method otherMethod = superClass.getMethods("clone").get(0);

                               assertTrue(method.overrides(superMethod));
                               assertFalse(method.overrides(otherMethod));
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
   void getParameters()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class example = context.getClassOrThrow("MethodExample");
                               Ap.Method method = example.getMethods("varArgsMethod").get(0);
                               String parameters = method.getParameters()
                                                         .stream()
                                                         .map(Ap.Nameable::getName)
                                                         .collect(joining());

                               assertEquals("args", parameters);
                            })
                   .withCodeToCompile("MethodExample.java", "public class MethodExample {private void varArgsMethod(String... args) {}}")
                   .compile();
   }

   @Test
   void getParameterTypes()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class string = context.getClassOrThrow("java.lang.String");
                               Ap.Array stringArray = string.asArray();
                               Ap.Class example = context.getClassOrThrow("MethodExample");
                               Ap.Method method = example.getMethods("varArgsMethod").get(0);
                               assertEquals(List.of(stringArray), method.getParameterTypes());
                            })
                   .withCodeToCompile("MethodExample.java", "public class MethodExample {private void varArgsMethod(String... args) {}}")
                   .compile();
   }

   @Test
   void getReturnType()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class string = context.getClassOrThrow("java.lang.String");
                               Ap.Class aLong = context.getClassOrThrow("java.lang.Long");
                               Ap.Method toString = string.getMethods("toString").get(0);
                               Ap.Type returnType = toString.getReturnType();
                               assertEquals(string, returnType);
                               assertNotEquals(aLong, returnType);
                            })
                   .compile();
   }

   @Test
   void getThrows()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class object = context.getClassOrThrow("java.lang.Object");
                               Ap.Class interruptedException = context.getClassOrThrow("java.lang.InterruptedException");
                               Ap.Method toString = object.getMethods("toString").get(0);
                               Ap.Method wait = object.getMethods("wait").get(0);

                               assertEquals(emptyList(), toString.getThrows());
                               assertEquals(List.of(interruptedException), wait.getThrows());
                            })
                   .compile();
   }

   @Test
   void isNotVarArgs()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class object = context.getClassOrThrow("java.lang.Object");
                               Ap.Method toString = object.getMethods("toString").get(0);
                               assertFalse(toString.isVarArgs());
                            })
                   .compile();
   }

   @Test
   void isVarArgs()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class object = context.getClassOrThrow("MethodExample");
                               Ap.Method method = object.getMethods("varArgsMethod").get(0);
                               assertTrue(method.isVarArgs());
                            })
                   .withCodeToCompile("MethodExample.java", "public class MethodExample {private void varArgsMethod(String... args) {}}")
                   .compile();
   }

   @Test
   void getSurrounding()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class example = context.getClassOrThrow("MethodExample");
                               Ap.Method toString = example.getMethods("toString").get(0);
                               assertEquals(example, toString.getSurrounding());
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
   void getReceiverType()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class example = context.getClassOrThrow("MethodExample");
                               Ap.Method toString = example.getMethods("toString").get(0);
                               assertTrue(toString.getReceiverType().isEmpty());

                               Ap.Method method = example.getMethods("receiver").get(0);
                               assertEquals(example, method.getReceiverType().orElseThrow());
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
