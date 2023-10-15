package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstructorRendererTest
{
   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals("public ConstructorExample(Long id) {}\n",
                                            render(DEFAULT, shadowApi.getClassOrThrow("ConstructorExample")
                                                            .getConstructors()
                                                            .get(0)).declaration());
                               assertEquals("public ConstructorExample(Long id) {\ntest\n}\n",
                                            render(DEFAULT, shadowApi.getClassOrThrow("ConstructorExample")
                                                            .getConstructors()
                                                            .get(0)).declaration("test"));

                               assertEquals("@TestAnnotation\npublic ConstructorExample(String name) throws java.io.IOException {}\n",
                                            render(DEFAULT, shadowApi.getClassOrThrow("ConstructorExample")
                                                            .getConstructors()
                                                            .get(1)).declaration());
                               assertEquals("""
                                                  @TestAnnotation
                                                  public ConstructorExample(String name) throws java.io.IOException {
                                                  test
                                                  }
                                                  """,
                                            render(DEFAULT, shadowApi.getClassOrThrow("ConstructorExample")
                                                            .getConstructors()
                                                            .get(1)).declaration("test"));

                               assertEquals("public ConstructorExample(String... names) {}\n",
                                            render(DEFAULT, shadowApi.getClassOrThrow("ConstructorExample")
                                                            .getConstructors()
                                                            .get(2)).declaration());
                               assertEquals("public ConstructorExample(String... names) {\ntest\n}\n",
                                            render(DEFAULT, shadowApi.getClassOrThrow("ConstructorExample")
                                                            .getConstructors()
                                                            .get(2)).declaration("test"));

                               assertEquals("public <T> ConstructorExample(T t) {}\n",
                                            render(DEFAULT, shadowApi.getClassOrThrow("ConstructorExample")
                                                            .getConstructors()
                                                            .get(3)).declaration());
                               assertEquals("public <T> ConstructorExample(T t) {\ntest\n}\n",
                                            render(DEFAULT, shadowApi.getClassOrThrow("ConstructorExample")
                                                            .getConstructors()
                                                            .get(3)).declaration("test"));

                               assertEquals("public Inner(ReceiverExample ReceiverExample.this) {}\n",
                                            render(DEFAULT, shadowApi.getClassOrThrow("ReceiverExample.Inner")
                                                            .getConstructors()
                                                            .get(0))
                                                  .declaration());
                            })
                   .withCodeToCompile("ConstructorExample.java", """
                         public class ConstructorExample {
                            public ConstructorExample(Long id) {}
                            @TestAnnotation
                            public ConstructorExample(String name) throws java.io.IOException {}
                            public ConstructorExample(String... names) {}
                            public <T> ConstructorExample(T t) {}
                         }
                         """)
                   .withCodeToCompile("ReceiverExample.java", """
                         public class ReceiverExample {
                            public class Inner {
                               public Inner(ReceiverExample ReceiverExample.this) {}
                            }
                         }
                         """)
                   .withCodeToCompile("TestAnnotation.java", """
                         public @interface TestAnnotation {
                         }
                         """)
                   .compile();
   }

   @Test
   void invocation()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals("ConstructorExample()",
                                            render(DEFAULT, shadowApi.getClassOrThrow("ConstructorExample").getConstructors().get(0)).invocation());
                               assertEquals("ConstructorExample(test)",
                                            render(DEFAULT, shadowApi.getClassOrThrow("ConstructorExample").getConstructors().get(0)).invocation("test"));
                            })
                   .withCodeToCompile("ConstructorExample.java", """
                         public class ConstructorExample {
                            public ConstructorExample(Long id) {}
                         }
                         """)
                   .compile();
   }
}