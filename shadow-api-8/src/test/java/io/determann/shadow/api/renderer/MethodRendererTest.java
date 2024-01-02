package io.determann.shadow.api.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.consistency.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MethodRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.compileTime(context -> context.getClassOrThrow("MethodExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("MethodExample")))
                     .withCode("MethodExample.java", "public abstract class MethodExample {\n" +
                                                     "   @MyAnnotation\n" +
                                                     "   abstract <T> void varArgsMethod(String... args) throws java.io.FileNotFoundException;\n" +
                                                     "   public void six(java.util.List list) {};\n" +
                                                     "   public abstract void seven(java.util.List<String> strings);\n" +
                                                     "}\n")
                     .withCode("MyAnnotation.java",
                               "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface MyAnnotation {} ")
                     .test(aClass ->
                           {
                              assertEquals(
                                    "@MyAnnotation\nabstract <T> void varArgsMethod(String... args) throws java.io.FileNotFoundException;\n",
                                    render(DEFAULT, aClass.getMethods("varArgsMethod").get(0)).declaration());
                              assertEquals("public void six(java.util.List list) {}\n",
                                           render(DEFAULT, aClass.getMethods("six").get(0)).declaration());
                              assertEquals("public void seven(java.util.List<String> strings) {\ntest\n}\n",
                                           render(DEFAULT, aClass.getMethods("seven").get(0)).declaration("test"));
                           },
                           aClass ->
                           {
                              assertEquals(
                                    "@MyAnnotation\nabstract <T> void varArgsMethod(String... arg0) throws java.io.FileNotFoundException;\n",
                                    render(DEFAULT, aClass.getMethods("varArgsMethod").get(0)).declaration());
                              assertEquals("public void six(java.util.List arg0) {}\n",
                                           render(DEFAULT, aClass.getMethods("six").get(0)).declaration());
                              assertEquals("public void seven(java.util.List<String> arg0) {\ntest\n}\n",
                                           render(DEFAULT, aClass.getMethods("seven").get(0)).declaration("test"));
                           });

      ConsistencyTest.compileTime(context -> context.getClassOrThrow("ReceiverExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("ReceiverExample")))
                     .withCode("ReceiverExample.java", "public class ReceiverExample {\n" +
                                                       "   private void receiver(@MyAnnotation ReceiverExample ReceiverExample.this) {}\n" +
                                                       "}\n")
                     .withCode("MyAnnotation.java",
                               "@java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE_USE)\n" +
                               "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n" +
                               "@interface MyAnnotation {}")
                     .test(aClass -> assertEquals("private void receiver(ReceiverExample ReceiverExample.this) {}\n",
                                                  render(DEFAULT, aClass.getMethods().get(0)).declaration()));
   }

   @Test
   void invocation()
   {
      ConsistencyTest.compileTime(context -> context.getClassOrThrow("MethodExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("MethodExample")))
                     .withCode("MethodExample.java", "public abstract class MethodExample {\n" +
                                                     "   abstract <T> void varArgsMethod(String... args);\n" +
                                                     "   public void six(java.util.List list) {};\n" +
                                                     "   public abstract void seven(java.util.List<String> strings);\n" +
                                                     "}\n")
                     .test(aClass ->
                           {
                              assertEquals("varArgsMethod()", render(DEFAULT, aClass.getMethods("varArgsMethod").get(0)).invocation());
                              assertEquals("six()", render(DEFAULT, aClass.getMethods("six").get(0)).invocation());
                              assertEquals("seven(test)", render(DEFAULT, aClass.getMethods("seven").get(0)).invocation("test"));
                           });
   }
}