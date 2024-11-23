package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.tck.internal.RenderingTestBuilder;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.DECLARED_GET_METHODS;
import static io.determann.shadow.api.Operations.EXECUTABLE_GET_PARAMETERS;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.RenderingTestBuilder.renderingTest;

class ParameterRendererTest
{
   @Test
   void declaration()
   {
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

      RenderingTestBuilder<C_Interface> parameterTest = renderingTest(C_Interface.class)
            .withSource("ParameterExample.java", """
                  public interface ParameterExample {
                     void foo(@MyAnnotation Long foo, Long... foo2);
                  }
                  """)
            .withSource("MyAnnotation.java",
                        "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME) @interface MyAnnotation {} ")
            .withToRender("ParameterExample");

      parameterTest.withRender(cInterface ->
                               {
                                  C_Method method = requestOrThrow(cInterface, DECLARED_GET_METHODS).get(0);
                                  C_Parameter parameter = requestOrThrow(method, EXECUTABLE_GET_PARAMETERS).get(0);
                                  return render(DEFAULT, parameter).declaration();
                               })
                   .withExpected("@MyAnnotation Long foo")
                   .test();

      parameterTest.withRender(cInterface ->
                               {
                                  C_Method method = requestOrThrow(cInterface, DECLARED_GET_METHODS).get(0);
                                  C_Parameter parameter = requestOrThrow(method, EXECUTABLE_GET_PARAMETERS).get(1);
                                  return render(DEFAULT, parameter).declaration();
                               })
                   .withExpected("Long... foo2")
                   .test();
   }
}