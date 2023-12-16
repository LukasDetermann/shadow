package io.determann.shadow.api.shadow;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
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
                   .withCodeToCompile("SubSignature.java", "        import java.util.List;\n" +
                                                           "\n" +
                                                           "                           public abstract class SubSignature {\n" +
                                                           "                              public abstract void first();\n" +
                                                           "                              public abstract void second();\n" +
                                                           "                              public abstract void third(String name, Long id);\n" +
                                                           "                              public abstract void four(Long id, String name);\n" +
                                                           "                              public abstract void five(Long id, String name2);\n" +
                                                           "                              public abstract void six(List list);\n" +
                                                           "                              public abstract void seven(List<String> strings);\n" +
                                                           "                           }")
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
                   .withCodeToCompile("MethodExample.java", "                           public class MethodExample {\n" +
                                                            "                              private void varArgsMethod(String... args) {}\n" +
                                                            "\n" +
                                                            "                              private void receiver(MethodExample MethodExample.this) {}\n" +
                                                            "\n" +
                                                            "                              @Override\n" +
                                                            "                              public String toString()\n" +
                                                            "                              {\n" +
                                                            "                                 return \"MethodExample{}\";\n" +
                                                            "                              }\n" +
                                                            "                           }")
                   .compile();
   }

   @Test
   @Override
   void testGetParameters()
   {
      ProcessorTest.process(shadowApi -> assertEquals("[args]",
                                                      shadowApi.getClassOrThrow("MethodExample")
                                                               .getMethods("varArgsMethod").get(0).getParameters().toString()))
                   .withCodeToCompile("MethodExample.java", "                           public class MethodExample {\n" +
                                                            "                              private void varArgsMethod(String... args) {}\n" +
                                                            "\n" +
                                                            "                              private void receiver(MethodExample MethodExample.this) {}\n" +
                                                            "\n" +
                                                            "                              @Override\n" +
                                                            "                              public String toString()\n" +
                                                            "                              {\n" +
                                                            "                                 return \"MethodExample{}\";\n" +
                                                            "                              }\n" +
                                                            "                           }")
                   .compile();
   }

   @Test
   @Override
   void testGetParameterTypes()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals(Collections.singletonList(shadowApi.asArray(shadowApi.getClassOrThrow("java.lang.String"))),
                                               shadowApi.getClassOrThrow("MethodExample")
                                                        .getMethods("varArgsMethod").get(0).getParameterTypes()))
                   .withCodeToCompile("MethodExample.java", "                           public class MethodExample {\n" +
                                                            "                              private void varArgsMethod(String... args) {}\n" +
                                                            "\n" +
                                                            "                              private void receiver(MethodExample MethodExample.this) {}\n" +
                                                            "\n" +
                                                            "                              @Override\n" +
                                                            "                              public String toString()\n" +
                                                            "                              {\n" +
                                                            "                                 return \"MethodExample{}\";\n" +
                                                            "                              }\n" +
                                                            "                           }")
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
                               assertEquals(Collections.emptyList(),
                                            shadowApi.getClassOrThrow("java.lang.Object")
                                                     .getMethods("toString").get(0)
                                                     .getThrows());

                               assertEquals(Collections.singletonList(shadowApi.getClassOrThrow("java.lang.InterruptedException")),
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
                   .withCodeToCompile("MethodExample.java", "                           public class MethodExample {\n" +
                                                            "                              private void varArgsMethod(String... args) {}\n" +
                                                            "\n" +
                                                            "                              private void receiver(MethodExample MethodExample.this) {}\n" +
                                                            "\n" +
                                                            "                              @Override\n" +
                                                            "                              public String toString()\n" +
                                                            "                              {\n" +
                                                            "                                 return \"MethodExample{}\";\n" +
                                                            "                              }\n" +
                                                            "                           }")
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
                   .withCodeToCompile("MethodExample.java", "                           public class MethodExample {\n" +
                                                            "                              private void varArgsMethod(String... args) {}\n" +
                                                            "\n" +
                                                            "                              private void receiver(MethodExample MethodExample.this) {}\n" +
                                                            "\n" +
                                                            "                              @Override\n" +
                                                            "                              public String toString()\n" +
                                                            "                              {\n" +
                                                            "                                 return \"MethodExample{}\";\n" +
                                                            "                              }\n" +
                                                            "                           }")
                   .compile();
   }

   @Test
   @Override
   void testGetReceiverType()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Class aClass = shadowApi.getClassOrThrow("MethodExample");

                               assertTrue(!aClass.getMethods("toString").get(0).getReceiverType().isPresent());
                               assertEquals(aClass,
                                            aClass.getMethods("receiver").get(0).getReceiverType().orElseThrow(IllegalStateException::new));
                            })
                   .withCodeToCompile("MethodExample.java", "                           public class MethodExample {\n" +
                                                            "                              private void varArgsMethod(String... args) {}\n" +
                                                            "\n" +
                                                            "                              private void receiver(MethodExample MethodExample.this) {}\n" +
                                                            "\n" +
                                                            "                              @Override\n" +
                                                            "                              public String toString()\n" +
                                                            "                              {\n" +
                                                            "                                 return \"MethodExample{}\";\n" +
                                                            "                              }\n" +
                                                            "                           }")
                   .compile();
   }
}
