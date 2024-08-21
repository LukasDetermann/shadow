package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.lang_model.LM_Queries;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.R_Queries;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.DECLARED_GET_CONSTRUCTORS;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstructorRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.<C_Class>compileTime(context -> context.getClassOrThrow("ConstructorExample"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("ConstructorExample")))
                     .withCode("ConstructorExample.java", """
                           public class ConstructorExample {
                              public ConstructorExample(Long id) {}
                           }
                             """)
                     .test(aClass ->
                           {
                              assertEquals("public ConstructorExample(Long id) {}\n",
                                           render(DEFAULT, LM_Queries.query(aClass).getConstructors().get(0)).declaration());
                              assertEquals("public ConstructorExample(Long id) {\ntest\n}\n",
                                           render(DEFAULT, LM_Queries.query(aClass).getConstructors().get(0)).declaration("test"));
                           },
                           aClass ->
                           {
                              assertEquals("public ConstructorExample(Long arg0) {}\n",
                                           render(DEFAULT, R_Queries.query(aClass).getConstructors().get(0)).declaration());
                              assertEquals("public ConstructorExample(Long arg0) {\ntest\n}\n",
                                           render(DEFAULT, R_Queries.query(aClass).getConstructors().get(0)).declaration("test"));
                           });

      ConsistencyTest.<C_Class>compileTime(context -> context.getClassOrThrow("ConstructorExample"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("ConstructorExample")))
                     .withCode("ConstructorExample.java", """
                           public class ConstructorExample {
                              @TestAnnotation
                              public ConstructorExample(String name) throws java.io.IOException {}
                           }
                           """)
                     .withCode("TestAnnotation.java", """
                           @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                           public @interface TestAnnotation {
                           }
                           """)
                     .test(aClass ->
                           {
                              assertEquals("@TestAnnotation\npublic ConstructorExample(String name) throws java.io.IOException {}\n",
                                           render(DEFAULT, LM_Queries.query(aClass).getConstructors().get(0)).declaration());
                              assertEquals("""
                                                 @TestAnnotation
                                                 public ConstructorExample(String name) throws java.io.IOException {
                                                 test
                                                 }
                                                 """,
                                           render(DEFAULT, LM_Queries.query(aClass).getConstructors().get(0)).declaration("test"));
                           },
                           aClass ->
                           {
                              assertEquals("@TestAnnotation\npublic ConstructorExample(String arg0) throws java.io.IOException {}\n",
                                           render(DEFAULT, R_Queries.query(aClass).getConstructors().get(0)).declaration());
                              assertEquals("""
                                                 @TestAnnotation
                                                 public ConstructorExample(String arg0) throws java.io.IOException {
                                                 test
                                                 }
                                                 """,
                                           render(DEFAULT, R_Queries.query(aClass).getConstructors().get(0)).declaration("test"));
                           });

      ConsistencyTest.<C_Class>compileTime(context -> context.getClassOrThrow("ConstructorExample"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("ConstructorExample")))
                     .withCode("ConstructorExample.java", """
                           public class ConstructorExample {
                              public ConstructorExample(String... names) {}
                           }
                           """)
                     .test(aClass ->
                           {
                              assertEquals("public ConstructorExample(String... names) {}\n",
                                           render(DEFAULT, LM_Queries.query(aClass).getConstructors().get(0)).declaration());
                              assertEquals("public ConstructorExample(String... names) {\ntest\n}\n",
                                           render(DEFAULT, LM_Queries.query(aClass).getConstructors().get(0)).declaration("test"));
                           },
                           aClass ->
                           {
                              assertEquals("public ConstructorExample(String... arg0) {}\n",
                                           render(DEFAULT, R_Queries.query(aClass).getConstructors().get(0)).declaration());
                              assertEquals("public ConstructorExample(String... arg0) {\ntest\n}\n",
                                           render(DEFAULT, R_Queries.query(aClass).getConstructors().get(0)).declaration("test"));
                           });

      ConsistencyTest.<C_Class>compileTime(context -> context.getClassOrThrow("ConstructorExample"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("ConstructorExample")))
                     .withCode("ConstructorExample.java", """
                           public class ConstructorExample {
                              public <T> ConstructorExample(T t) {}
                           }
                           """)
                     .test(aClass ->
                           {
                              assertEquals("public <T> ConstructorExample(T t) {\ntest\n}\n",
                                           render(DEFAULT, LM_Queries.query(aClass).getConstructors().get(0)).declaration("test"));
                              assertEquals("public <T> ConstructorExample(T t) {}\n",
                                           render(DEFAULT, LM_Queries.query(aClass).getConstructors().get(0)).declaration());
                           },
                           aClass ->
                           {
                              assertEquals("public <T> ConstructorExample(T arg0) {}\n",
                                           render(DEFAULT, R_Queries.query(aClass).getConstructors().get(0)).declaration());
                              assertEquals("public <T> ConstructorExample(T arg0) {\ntest\n}\n",
                                           render(DEFAULT, R_Queries.query(aClass).getConstructors().get(0)).declaration("test"));
                           });

      ConsistencyTest.<C_Class>compileTime(context -> context.getClassOrThrow("ReceiverExample.Inner"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("ReceiverExample$Inner")))
                     .withCode("ReceiverExample.java", """
                           public class ReceiverExample {
                              public class Inner {
                                 public Inner(@MyAnnotation ReceiverExample ReceiverExample.this) {}
                              }
                           }
                           """)
                     .withCode("MyAnnotation.java",
                               """
                                     @java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE_USE)
                                     @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                     @interface MyAnnotation {}""")
                     .test(aClass -> assertEquals("public Inner(ReceiverExample ReceiverExample.this) {}\n",
                                                  render(DEFAULT, requestOrThrow(aClass, DECLARED_GET_CONSTRUCTORS).get(0)).declaration()));
   }

   @Test
   void invocation()
   {
      ConsistencyTest.<C_Class>compileTime(context -> context.getClassOrThrow("ConstructorExample"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("ConstructorExample")))
                     .withCode("ConstructorExample.java", """
                           public class ConstructorExample {
                              public ConstructorExample(Long id) {}
                           }
                           """)
                     .test(aClass ->
                           {
                              assertEquals("ConstructorExample()", render(DEFAULT, requestOrThrow(aClass, DECLARED_GET_CONSTRUCTORS).get(0)).invocation());

                              assertEquals("ConstructorExample(test)", render(DEFAULT, requestOrThrow(aClass, DECLARED_GET_CONSTRUCTORS).get(0)).invocation("test"));
                           });
   }
}