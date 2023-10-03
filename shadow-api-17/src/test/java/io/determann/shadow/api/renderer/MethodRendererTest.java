package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.ShadowApi.render;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MethodRendererTest
{
   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               List<Method> methods = shadowApi.getClassOrThrow("MethodExample").getMethods();

                               assertEquals(
                                     "@MyAnnotation\nabstract <T> void varArgsMethod(String... args) throws java.io.FileNotFoundException;\n",
                                     render(methods.get(0)).declaration());
                               assertEquals("public void six(java.util.List list) {}\n",
                                            render(methods.get(1)).declaration());
                               assertEquals("public void seven(java.util.List<String> strings) {\ntest\n}\n",
                                            render(methods.get(2)).declaration("test"));

                               assertEquals("private void receiver(ReceiverExample ReceiverExample.this) {}\n",
                                            render(shadowApi.getClassOrThrow("ReceiverExample")
                                                            .getMethods()
                                                            .get(0))
                                                  .declaration());
                            })
                   .withCodeToCompile("MethodExample.java", """
                         public abstract class MethodExample {
                            @MyAnnotation
                            abstract <T> void varArgsMethod(String... args) throws java.io.FileNotFoundException;
                            public void six(java.util.List list) {};
                            public abstract void seven(java.util.List<String> strings);
                         }
                         """)
                   .withCodeToCompile("MyAnnotation.java", "@interface MyAnnotation {} ")
                   .withCodeToCompile("ReceiverExample.java", """
                         public class ReceiverExample {
                            private void receiver(ReceiverExample ReceiverExample.this) {}
                         }
                         """)
                   .compile();
   }

   @Test
   void invocation()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               List<Method> methods = shadowApi.getClassOrThrow("MethodExample").getMethods();

                               assertEquals("varArgsMethod()",
                                            render(methods.get(0)).invocation());
                               assertEquals("six()",
                                            render(methods.get(1)).invocation());
                               assertEquals("seven(test)",
                                            render(methods.get(2)).invocation("test"));
                            })
                   .withCodeToCompile("MethodExample.java", """
                         public abstract class MethodExample {
                            abstract <T> void varArgsMethod(String... args);
                            public void six(java.util.List list) {};
                            public abstract void seven(java.util.List<String> strings);
                         }
                         """)
                   .compile();
   }
}