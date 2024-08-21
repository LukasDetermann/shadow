package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.lang_model.LangModelQueries;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.ReflectionQueries;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.DECLARED_GET_METHOD;
import static io.determann.shadow.api.Operations.DECLARED_GET_METHODS;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MethodRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.<Class>compileTime(context -> context.getClassOrThrow("MethodExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("MethodExample")))
                     .withCode("MethodExample.java", """
                           public abstract class MethodExample {
                              @MyAnnotation
                              abstract <T> void varArgsMethod(String... args) throws java.io.FileNotFoundException;
                              public void six(java.util.List list) {};
                              public abstract void seven(java.util.List<String> strings);
                           }
                           """)
                     .withCode("MyAnnotation.java",
                               "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface MyAnnotation {} ")
                     .test(aClass ->
                           {
                              assertEquals(
                                    "@MyAnnotation\nabstract <T> void varArgsMethod(String... args) throws java.io.FileNotFoundException;\n",
                                    render(DEFAULT, LangModelQueries.query(aClass).getMethods("varArgsMethod").get(0)).declaration());
                              assertEquals("public void six(java.util.List list) {}\n",
                                           render(DEFAULT, LangModelQueries.query(aClass).getMethods("six").get(0)).declaration());
                              assertEquals("public void seven(java.util.List<String> strings) {\ntest\n}\n",
                                           render(DEFAULT, LangModelQueries.query(aClass).getMethods("seven").get(0)).declaration("test"));
                           },
                           aClass ->
                           {
                              assertEquals(
                                    "@MyAnnotation\nabstract <T> void varArgsMethod(String... arg0) throws java.io.FileNotFoundException;\n",
                                    render(DEFAULT, ReflectionQueries.query(aClass).getMethods("varArgsMethod").get(0)).declaration());
                              assertEquals("public void six(java.util.List arg0) {}\n",
                                           render(DEFAULT, ReflectionQueries.query(aClass).getMethods("six").get(0)).declaration());
                              assertEquals("public void seven(java.util.List<String> arg0) {\ntest\n}\n",
                                           render(DEFAULT, ReflectionQueries.query(aClass).getMethods("seven").get(0)).declaration("test"));
                           });

      ConsistencyTest.<Class>compileTime(context -> context.getClassOrThrow("ReceiverExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("ReceiverExample")))
                     .withCode("ReceiverExample.java", """
                           public class ReceiverExample {
                              private void receiver(@MyAnnotation ReceiverExample ReceiverExample.this) {}
                           }
                           """)
                     .withCode("MyAnnotation.java",
                               """
                                     @java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE_USE)
                                     @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                     @interface MyAnnotation {}""")
                     .test(aClass -> assertEquals("private void receiver(ReceiverExample ReceiverExample.this) {}\n",
                                                  render(DEFAULT, requestOrThrow(aClass, DECLARED_GET_METHODS).get(0)).declaration()));
   }

   @Test
   void invocation()
   {
      ConsistencyTest.<Class>compileTime(context -> context.getClassOrThrow("MethodExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("MethodExample")))
                     .withCode("MethodExample.java", """
                           public abstract class MethodExample {
                              abstract <T> void varArgsMethod(String... args);
                              public void six(java.util.List list) {};
                              public abstract void seven(java.util.List<String> strings);
                           }
                           """)
                     .test(aClass ->
                           {
                              assertEquals("varArgsMethod()", render(DEFAULT, requestOrThrow(aClass, DECLARED_GET_METHOD,"varArgsMethod").get(0)).invocation());
                              assertEquals("six()", render(DEFAULT, requestOrThrow(aClass, DECLARED_GET_METHOD, "six").get(0)).invocation());
                              assertEquals("seven(test)", render(DEFAULT, requestOrThrow(aClass, DECLARED_GET_METHOD, "seven").get(0)).invocation("test"));
                           });
   }
}