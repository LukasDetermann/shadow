package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.tck.internal.RenderingTestBuilder;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.DECLARED_GET_METHOD;
import static io.determann.shadow.api.Operations.DECLARED_GET_METHODS;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.RenderingTestBuilder.renderingTest;

class MethodRendererTest
{
   @Test
   void declaration()
   {
      RenderingTestBuilder<C_Class> methodTest = renderingTest(C_Class.class)
            .withSource("MethodExample.java", """
                  public abstract class MethodExample {
                     @MyAnnotation
                     abstract <T> void varArgsMethod(String... args) throws java.io.FileNotFoundException;
                     public void six(java.util.List list) {};
                     public abstract void seven(java.util.List<String> strings);
                  }
                  """)
            .withSource("MyAnnotation.java",
                        "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface MyAnnotation {}")
            .withToRender("MethodExample");

      methodTest.withRender(cClass ->
                            {
                               C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "varArgsMethod").get(0);
                               return render(DEFAULT, method).declaration();
                            })
                .withExpected("@MyAnnotation\nabstract <T> void varArgsMethod(String... args) throws java.io.FileNotFoundException;\n")
                .test();

      methodTest.withRender(cClass ->
                            {
                               C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "six").get(0);
                               return render(DEFAULT, method).declaration();
                            })
                .withExpected("public void six(java.util.List list) {}\n")
                .test();

      methodTest.withRender(cClass ->
                            {
                               C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "seven").get(0);
                               return render(DEFAULT, method).declaration("test");
                            })
                .withExpected("public void seven(java.util.List<String> strings) {\ntest\n}\n")
                .test();

      renderingTest(C_Class.class)
            .withSource("ReceiverExample.java", """
                  public class ReceiverExample {
                     private void receiver(@MyAnnotation ReceiverExample ReceiverExample.this) {}
                  }
                  """)
            .withSource("MyAnnotation.java",
                        """
                              @java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE_USE)
                              @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                              @interface MyAnnotation {}""")
            .withToRender("ReceiverExample")
            .withRender(cClass ->
                        {
                           C_Method method = requestOrThrow(cClass, DECLARED_GET_METHODS).get(0);
                           return render(DEFAULT, method).declaration();
                        })
            .withExpected("private void receiver(ReceiverExample ReceiverExample.this) {}\n")
            .test();
   }

   @Test
   void invocation()
   {
      RenderingTestBuilder<C_Class> methodTest = renderingTest(C_Class.class)
            .withSource("MethodExample.java", """
                  public abstract class MethodExample {
                     abstract <T> void varArgsMethod(String... args);
                     public void six(java.util.List list) {};
                     public abstract void seven(java.util.List<String> strings);
                  }
                  """)
            .withToRender("MethodExample");

      methodTest.withRender(cClass ->
                            {
                               C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "varArgsMethod").get(0);
                               return render(DEFAULT, method).invocation();
                            })
                .withExpected("varArgsMethod()")
                .test();

      methodTest.withRender(cClass ->
                            {
                               C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "six").get(0);
                               return render(DEFAULT, method).invocation();
                            })
                .withExpected("six()")
                .test();

      methodTest.withRender(cClass ->
                            {
                               C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "seven").get(0);
                               return render(DEFAULT, method).invocation("test");
                            })
                .withExpected("seven(test)")
                .test();
   }
}