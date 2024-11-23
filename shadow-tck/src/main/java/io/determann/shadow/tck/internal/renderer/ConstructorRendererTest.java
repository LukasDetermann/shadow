package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.structure.C_Constructor;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.tck.internal.RenderingTestBuilder;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.DECLARED_GET_CONSTRUCTORS;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.RenderingTestBuilder.renderingTest;

class ConstructorRendererTest
{
   @Test
   void declaration()
   {
      RenderingTestBuilder<C_Class> paramExample = renderingTest(C_Class.class).withSource("ConstructorExample.java", """
                                                                                     public class ConstructorExample {
                                                                                        public ConstructorExample(Long id) {}
                                                                                     }
                                                                                     """)
                                                                               .withToRender("ConstructorExample");

      paramExample.withRender(cClass ->
                              {
                                 C_Constructor constructor = requestOrThrow(cClass, DECLARED_GET_CONSTRUCTORS).get(0);
                                 return render(DEFAULT, constructor).declaration();
                              })
                  .withExpected("public ConstructorExample(Long id) {}\n")
                  .test();

      paramExample.withRender(cClass ->
                              {
                                 C_Constructor constructor = requestOrThrow(cClass, DECLARED_GET_CONSTRUCTORS).get(0);
                                 return render(DEFAULT, constructor).declaration("test");
                              })
                  .withExpected("public ConstructorExample(Long id) {\ntest\n}\n")
                  .test();

      RenderingTestBuilder<C_Class> throwsTest = renderingTest(C_Class.class).withSource("ConstructorExample.java", """
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
                                                                             .withToRender("ConstructorExample");

      throwsTest.withRender(cClass ->
                            {
                               C_Constructor constructor = requestOrThrow(cClass, DECLARED_GET_CONSTRUCTORS).get(0);
                               return render(DEFAULT, constructor).declaration();
                            })
                .withExpected("@TestAnnotation\npublic ConstructorExample(String name) throws java.io.IOException {}\n")
                .test();

      throwsTest.withRender(cClass ->
                            {
                               C_Constructor constructor = requestOrThrow(cClass, DECLARED_GET_CONSTRUCTORS).get(0);
                               return render(DEFAULT, constructor).declaration("test");
                            })
                .withExpected("""
                                    @TestAnnotation
                                    public ConstructorExample(String name) throws java.io.IOException {
                                    test
                                    }
                                    """)
                .test();

      RenderingTestBuilder<C_Class> varargsTest = renderingTest(C_Class.class).withSource("ConstructorExample.java", """
                                                                                    public class ConstructorExample {
                                                                                       public ConstructorExample(String... names) {}
                                                                                    }
                                                                                    """)
                                                                              .withToRender("ConstructorExample");

      varargsTest.withRender(cClass ->
                             {
                                C_Constructor constructor = requestOrThrow(cClass, DECLARED_GET_CONSTRUCTORS).get(0);
                                return render(DEFAULT, constructor).declaration();
                             })
                 .withExpected("public ConstructorExample(String... names) {}\n")
                 .test();

      varargsTest.withRender(cClass ->
                             {
                                C_Constructor constructor = requestOrThrow(cClass, DECLARED_GET_CONSTRUCTORS).get(0);
                                return render(DEFAULT, constructor).declaration("test");
                             })
                 .withExpected("public ConstructorExample(String... names) {\ntest\n}\n")
                 .test();

      RenderingTestBuilder<C_Class> genericTest = renderingTest(C_Class.class).withSource("ConstructorExample.java", """
                                                                                    public class ConstructorExample {
                                                                                       public <T> ConstructorExample(T t) {}
                                                                                    }
                                                                                    """)
                                                                              .withToRender("ConstructorExample");

      genericTest.withRender(cClass ->
                             {
                                C_Constructor constructor = requestOrThrow(cClass, DECLARED_GET_CONSTRUCTORS).get(0);
                                return render(DEFAULT, constructor).declaration();
                             })
                 .withExpected("public <T> ConstructorExample(T t) {}\n")
                 .test();

      genericTest.withRender(cClass ->
                             {
                                C_Constructor constructor = requestOrThrow(cClass, DECLARED_GET_CONSTRUCTORS).get(0);
                                return render(DEFAULT, constructor).declaration("test");
                             })
                 .withExpected("public <T> ConstructorExample(T t) {\ntest\n}\n")
                 .test();

      renderingTest(C_Class.class).withSource("ReceiverExample.java", """
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
                                  .withToRender("ReceiverExample.Inner")
                                  .withRender(cClass ->
                                              {
                                                 C_Constructor constructor = requestOrThrow(cClass, DECLARED_GET_CONSTRUCTORS).get(0);
                                                 return render(DEFAULT, constructor).declaration();
                                              })
                                  .withExpected("public Inner(ReceiverExample ReceiverExample.this) {}\n")
                                  .test();
   }

   @Test
   void invocation()
   {
      RenderingTestBuilder<C_Class> renderingTest = renderingTest(C_Class.class).withSource("ConstructorExample.java", """
                                                                                      public class ConstructorExample {
                                                                                         public ConstructorExample(Long id) {}
                                                                                      }
                                                                                      """)
                                                                                .withToRender("ConstructorExample");

      renderingTest.withRender(cClass ->
                               {
                                  C_Constructor constructor = requestOrThrow(cClass, DECLARED_GET_CONSTRUCTORS).get(0);
                                  return render(DEFAULT, constructor).invocation();
                               })
                   .withExpected("ConstructorExample()")
                   .test();

      renderingTest.withRender(cClass ->
                               {
                                  C_Constructor constructor = requestOrThrow(cClass, DECLARED_GET_CONSTRUCTORS).get(0);
                                  return render(DEFAULT, constructor).invocation("test");
                               })
                   .withExpected("ConstructorExample(test)")
                   .test();
   }
}