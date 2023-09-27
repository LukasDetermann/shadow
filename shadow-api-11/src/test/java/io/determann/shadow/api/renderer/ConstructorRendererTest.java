package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.ShadowApi.render;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstructorRendererTest
{
   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals("public ConstructorExample(Long id) {}\n",
                                                       render(shadowApi.getClassOrThrow("ConstructorExample")
                                                                       .getConstructors()
                                                                       .get(0)).declaration());
                               assertEquals("public ConstructorExample(Long id) {\ntest\n}\n",
                                                       render(shadowApi.getClassOrThrow("ConstructorExample")
                                                                       .getConstructors()
                                                                       .get(0)).declaration("test"));

                               assertEquals("@TestAnnotation\npublic ConstructorExample(String name) throws java.io.IOException {}\n",
                                                       render(shadowApi.getClassOrThrow("ConstructorExample")
                                                                       .getConstructors()
                                                                       .get(1)).declaration());
                               assertEquals("@TestAnnotation\n" +
                                            "public ConstructorExample(String name) throws java.io.IOException {\n" +
                                            "test\n" +
                                            "}\n",
                                            render(shadowApi.getClassOrThrow("ConstructorExample")
                                                                       .getConstructors()
                                                                       .get(1)).declaration("test"));

                               assertEquals("public ConstructorExample(String... names) {}\n",
                                                       render(shadowApi.getClassOrThrow("ConstructorExample")
                                                                       .getConstructors()
                                                                       .get(2)).declaration());
                               assertEquals("public ConstructorExample(String... names) {\ntest\n}\n",
                                                       render(shadowApi.getClassOrThrow("ConstructorExample")
                                                                       .getConstructors()
                                                                       .get(2)).declaration("test"));

                               assertEquals("public <T> ConstructorExample(T t) {}\n",
                                                       render(shadowApi.getClassOrThrow("ConstructorExample")
                                                                       .getConstructors()
                                                                       .get(3)).declaration());
                               assertEquals("public <T> ConstructorExample(T t) {\ntest\n}\n",
                                                       render(shadowApi.getClassOrThrow("ConstructorExample")
                                                                       .getConstructors()
                                                                       .get(3)).declaration("test"));

                               assertEquals("public Inner(ReceiverExample ReceiverExample.this) {}\n",
                                            render(shadowApi.getClassOrThrow("ReceiverExample.Inner")
                                                         .getConstructors()
                                                         .get(0))
                                                  .declaration());
                            })
                   .withCodeToCompile("ConstructorExample.java", "public class ConstructorExample {\n" +
                                                                 "   public ConstructorExample(Long id) {}\n" +
                                                                 "   @TestAnnotation\n" +
                                                                 "   public ConstructorExample(String name) throws java.io.IOException {}\n" +
                                                                 "   public ConstructorExample(String... names) {}\n" +
                                                                 "   public <T> ConstructorExample(T t) {}\n" +
                                                                 "}\n")
                   .withCodeToCompile("ReceiverExample.java", "public class ReceiverExample {\n" +
                                                              "   public class Inner {\n" +
                                                              "      public Inner(ReceiverExample ReceiverExample.this) {}\n" +
                                                              "   }\n" +
                                                              "}\n")
                   .withCodeToCompile("TestAnnotation.java", "public @interface TestAnnotation {\n}\n")
                   .compile();
   }

   @Test
   void invocation()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals("ConstructorExample()",
                                                       render(shadowApi.getClassOrThrow("ConstructorExample").getConstructors().get(0)).invocation());
                               assertEquals("ConstructorExample(test)",
                                                       render(shadowApi.getClassOrThrow("ConstructorExample").getConstructors().get(0)).invocation("test"));
                            })
                   .withCodeToCompile("ConstructorExample.java", "public class ConstructorExample {\n" +
                                                                 "   public ConstructorExample(Long id) {}\n" +
                                                                 "}\n")
                   .compile();
   }
}