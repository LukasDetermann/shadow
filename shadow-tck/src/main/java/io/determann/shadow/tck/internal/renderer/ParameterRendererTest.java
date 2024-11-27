package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Interface;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParameterRendererTest
{
   @Test
   void receiverDeclaration()
   {
      String expected = "private void receiver(ReceiverExample ReceiverExample.this) {}\n";

      withSource("ReceiverExample.java",
                 "public class ReceiverExample {private void receiver(@MyAnnotation ReceiverExample ReceiverExample.this) {}}")
            .withSource("MyAnnotation.java",
                        """
                              @java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE_USE)
                              @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                              @interface MyAnnotation {}""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "ReceiverExample");
                     C_Method method = requestOrThrow(cClass, DECLARED_GET_METHODS).get(0);
                     assertEquals(expected, render(DEFAULT, method).declaration());
                  });
   }

   @Test
   void annotatedDeclaration()
   {
      withSource("ParameterExample.java", """
            public interface ParameterExample {
               void foo(@MyAnnotation Long foo);
            }
            """)
            .withSource("MyAnnotation.java",
                        "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME) @interface MyAnnotation {} ")
            .test(implementation ->
                  {
                     C_Interface cInterface = requestOrThrow(implementation, GET_INTERFACE, "ParameterExample");
                     C_Method method = requestOrThrow(cInterface, DECLARED_GET_METHODS).get(0);
                     C_Parameter parameter = requestOrThrow(method, EXECUTABLE_GET_PARAMETERS).get(0);
                     assertEquals("@MyAnnotation Long foo", render(DEFAULT, parameter).declaration());
                  });
   }

   @Test
   void varArgsDeclaration()
   {
      withSource("ParameterExample.java", """
            public interface ParameterExample {
               void foo(Long... foo);
            }
            """)
            .withSource("MyAnnotation.java",
                        "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME) @interface MyAnnotation {} ")
            .test(implementation ->
                  {
                     C_Interface cInterface = requestOrThrow(implementation, GET_INTERFACE, "ParameterExample");
                     C_Method method = requestOrThrow(cInterface, DECLARED_GET_METHODS).get(0);
                     C_Parameter parameter = requestOrThrow(method, EXECUTABLE_GET_PARAMETERS).get(0);
                     assertEquals("Long... foo", render(DEFAULT, parameter).declaration());
                  });
   }
}