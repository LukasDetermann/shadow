package org.determann.shadow.api.shadow;

import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MethodTest extends ExecutableTest<Method>
{
   MethodTest()
   {
      super(shadowApi -> shadowApi.getClass("java.lang.String").getMethods("toString").get(0));
   }

   @Test
   void testisSubSignatureOf()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 List<Method> methods = shadowApi.getClass("SubSignature").getMethods();
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
      CompilationTest.process(shadowApi ->
                              {
                                 assertTrue(shadowApi.getClass("MethodExample")
                                                     .getMethods("toString").get(0)
                                                     .overrides(shadowApi.getClass("java.lang.Object").getMethods("toString").get(0)));

                                 assertFalse(shadowApi.getClass("MethodExample")
                                                      .getMethods("toString").get(0)
                                                      .overrides(shadowApi.getClass("java.lang.Object").getMethods("clone").get(0)));
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
      CompilationTest.process(shadowApi -> assertEquals("[args]",
                                                        shadowApi.getClass("MethodExample")
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
      CompilationTest.process(shadowApi ->
                                    assertEquals(List.of(shadowApi.convert(shadowApi.getClass("java.lang.String")).asArray()),
                                                 shadowApi.getClass("MethodExample")
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
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(shadowApi.getClass("java.lang.String"),
                                              shadowApi.getClass("java.lang.String")
                                                       .getMethods("toString").get(0)
                                                       .getReturnType());

                                 assertNotEquals(shadowApi.getClass("java.lang.Long"),
                                                 shadowApi.getClass("java.lang.String")
                                                          .getMethods("toString").get(0)
                                                          .getReturnType());
                              })
                     .compile();
   }

   @Test
   @Override
   void testGetThrows()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(List.of(),
                                              shadowApi.getClass("java.lang.Object")
                                                       .getMethods("toString").get(0)
                                                       .getThrows());

                                 assertEquals(List.of(shadowApi.getClass("java.lang.InterruptedException")),
                                              shadowApi.getClass("java.lang.Object")
                                                       .getMethods("wait").get(0)
                                                       .getThrows());
                              })
                     .compile();
   }

   @Test
   @Override
   void testIsVarArgs()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertFalse(shadowApi.getClass("java.lang.Object")
                                                      .getMethods("toString").get(0)
                                                      .isVarArgs());
                                 assertTrue(shadowApi.getClass("MethodExample")
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
      CompilationTest.process(shadowApi ->
                              {
                                 Class aClass = shadowApi.getClass("MethodExample");
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
   void testGetPackage()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getPackages("org.determann.shadow.example.processed.test.method").get(0),
                                                        shadowApi.getClass("org.determann.shadow.example.processed.test.method.MethodExample")
                                                                 .getMethods("toString")
                                                                 .get(0)
                                                                 .getPackage()))
                     .withCodeToCompile("MethodExample.java",
                                        "                           package org.determann.shadow.example.processed.test.method;\n" +
                                        "\n" +
                                        "                           public class MethodExample {\n" +
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
      CompilationTest.process(shadowApi ->
                              {
                                 Class aClass = shadowApi.getClass("MethodExample");

                                 assertTrue(aClass.getMethods("toString").get(0).getReceiverType().isEmpty());
                                 assertEquals(aClass, aClass.getMethods("receiver").get(0).getReceiverType().orElseThrow());
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
