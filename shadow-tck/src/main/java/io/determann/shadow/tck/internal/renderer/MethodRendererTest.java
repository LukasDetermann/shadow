package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.type.C_Class;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.DECLARED_GET_METHOD;
import static io.determann.shadow.api.Operations.GET_CLASS;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MethodRendererTest
{
   @Test
   void varArgsDeclaration()
   {
      String expected = "@MyAnnotation\nabstract <T> void varArgsMethod(String... args) throws java.io.FileNotFoundException;\n";

      withSource("MethodExample.java", """
            public abstract class MethodExample {
               @MyAnnotation
               abstract <T> void varArgsMethod(String... args) throws java.io.FileNotFoundException;
            }
            """)
            .withSource("MyAnnotation.java",
                        "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface MyAnnotation {}")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "MethodExample");
                     C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "varArgsMethod").get(0);
                     assertEquals(expected, render(DEFAULT, method).declaration());
                  });
   }

   @Test
   void rawDeclaration()
   {
      withSource("MethodExample.java", "public abstract class MethodExample {public void six(java.util.List list) {};}")
            .withSource("MyAnnotation.java",
                        "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME) @interface MyAnnotation {}")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "MethodExample");
                     C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "six").get(0);
                     assertEquals("public void six(java.util.List list) {}\n", render(DEFAULT, method).declaration());
                  });
   }

   @Test
   void genericDeclaration()
   {
      String expected = "public void seven(java.util.List<String> strings) {\ntest\n}\n";

      withSource("MethodExample.java", "public abstract class MethodExample {public abstract void seven(java.util.List<String> strings);}")
            .withSource("MyAnnotation.java",
                        "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME) @interface MyAnnotation {}")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "MethodExample");
                     C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "seven").get(0);
                     assertEquals(expected, render(DEFAULT, method).declaration("test"));
                  });
   }

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
                     C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "receiver").get(0);
                     assertEquals(expected, render(DEFAULT, method).declaration());
                  });
   }

   @Test
   void varArgsInvocation()
   {
      withSource("MethodExample.java", "public abstract class MethodExample {abstract <T> void varArgsMethod(String... args);}")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "MethodExample");
                     C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "varArgsMethod").get(0);
                     assertEquals("varArgsMethod()", render(DEFAULT, method).invocation());
                  });
   }

   @Test
   void emptyInvocation()
   {
      withSource("MethodExample.java", "public abstract class MethodExample {public void six(java.util.List list) {};}")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "MethodExample");
                     C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "six").get(0);
                     assertEquals("six()", render(DEFAULT, method).invocation());
                  });
   }

   @Test
   void invocation()
   {
      withSource("MethodExample.java", "public abstract class MethodExample {public abstract void seven(java.util.List<String> strings);}")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "MethodExample");
                     C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "seven").get(0);
                     assertEquals("seven(test)", render(DEFAULT, method).invocation("test"));
                  });
   }
}