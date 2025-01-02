package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.structure.C_Constructor;
import io.determann.shadow.api.shadow.type.C_Class;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.CLASS_GET_CONSTRUCTORS;
import static io.determann.shadow.api.Operations.GET_CLASS;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstructorRendererTest
{
   @Test
   void emptyDeclaration()
   {
      withSource("ConstructorExample.java", """
            public class ConstructorExample {
               public ConstructorExample(Long id) {}
            }
            """)
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     C_Constructor constructor = requestOrThrow(cClass, CLASS_GET_CONSTRUCTORS).get(0);

                     assertEquals("public ConstructorExample(Long id) {}\n", render(DEFAULT, constructor).declaration());
                  });
   }

   @Test
   void declaration()
   {
      withSource("ConstructorExample.java", """
            public class ConstructorExample {
               public ConstructorExample(Long id) {}
            }
            """)
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     C_Constructor constructor = requestOrThrow(cClass, CLASS_GET_CONSTRUCTORS).get(0);

                     assertEquals("public ConstructorExample(Long id) {\ntest\n}\n", render(DEFAULT, constructor).declaration("test"));
                  });
   }

   @Test
   void exceptionDeclaration()
   {
      String expected = "public ConstructorExample(String name) throws java.io.IOException {}\n";

      withSource("ConstructorExample.java", """
            public class ConstructorExample {
               public ConstructorExample(String name) throws java.io.IOException {}
            }
            """)
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     C_Constructor constructor = requestOrThrow(cClass, CLASS_GET_CONSTRUCTORS).get(0);

                     assertEquals(expected, render(DEFAULT, constructor).declaration());
                  });
   }

   @Test
   void annotatedDeclaration()
   {
      String expected = """
            @TestAnnotation
            public ConstructorExample(String name) throws java.io.IOException {
            test
            }
            """;

      withSource("ConstructorExample.java", """
            public class ConstructorExample {
               @TestAnnotation
               public ConstructorExample(String name) throws java.io.IOException {}
            }
            """)
            .withSource("TestAnnotation.java", """
                  @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                  public @interface TestAnnotation {
                  }
                  """)
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     C_Constructor constructor = requestOrThrow(cClass, CLASS_GET_CONSTRUCTORS).get(0);

                     assertEquals(expected, render(DEFAULT, constructor).declaration("test"));
                  });
   }

   @Test
   void varargsDeclaration()
   {
      withSource("ConstructorExample.java", """
            public class ConstructorExample {
               public ConstructorExample(String... names) {}
            }
            """)
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     C_Constructor constructor = requestOrThrow(cClass, CLASS_GET_CONSTRUCTORS).get(0);

                     assertEquals("public ConstructorExample(String... names) {}\n", render(DEFAULT, constructor).declaration());
                  });
   }

   @Test
   void genericDeclaration()
   {
      withSource("ConstructorExample.java", """
            public class ConstructorExample {
               public <T> ConstructorExample(T t) {}
            }
            """)
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     C_Constructor constructor = requestOrThrow(cClass, CLASS_GET_CONSTRUCTORS).get(0);

                     assertEquals("public <T> ConstructorExample(T t) {}\n", render(DEFAULT, constructor).declaration());
                  });
   }

   @Test
   void receiverDeclaration()
   {
      withSource("ReceiverExample.java", """
            public class ReceiverExample {
               public class Inner {
                  public Inner(@MyAnnotation ReceiverExample ReceiverExample.this) {}
               }
            }
            """)
            .withSource("MyAnnotation.java",
                        """
                              @java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE_USE)
                              @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                              @interface MyAnnotation {}""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "ReceiverExample.Inner");
                     C_Constructor constructor = requestOrThrow(cClass, CLASS_GET_CONSTRUCTORS).get(0);

                     assertEquals("public Inner(ReceiverExample ReceiverExample.this) {}\n", render(DEFAULT, constructor).declaration());
                  });
   }

   @Test
   void emptyInvocation()
   {
      withSource("ConstructorExample.java", """
            public class ConstructorExample {
               public ConstructorExample(Long id) {}
            }
            """)
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     C_Constructor constructor = requestOrThrow(cClass, CLASS_GET_CONSTRUCTORS).get(0);
                     assertEquals("ConstructorExample()", render(DEFAULT, constructor).invocation());
                  });
   }

   @Test
   void invocation()
   {
      withSource("ConstructorExample.java", """
            public class ConstructorExample {
               public ConstructorExample(Long id) {}
            }
            """)
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "ConstructorExample");
                     C_Constructor constructor = requestOrThrow(cClass, CLASS_GET_CONSTRUCTORS).get(0);
                     assertEquals("ConstructorExample(test)", render(DEFAULT, constructor).invocation("test"));
                  });
   }
}