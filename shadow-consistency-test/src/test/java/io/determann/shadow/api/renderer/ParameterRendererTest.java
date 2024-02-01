package io.determann.shadow.api.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Parameter;
import io.determann.shadow.consistency.ConsistencyTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParameterRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.compileTime(context -> context.getClassOrThrow("ReceiverExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("ReceiverExample")))
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
                                                  render(DEFAULT, aClass.getMethods().get(0)).declaration()));


      ConsistencyTest.compileTime(context -> context.getInterfaceOrThrow("ParameterExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("ParameterExample")))
                     .withCode("ParameterExample.java", """
                         public interface ParameterExample {
                            void foo(@MyAnnotation Long foo, Long... foo2);
                         }
                         """)
                     .withCode("MyAnnotation.java", "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME) @interface MyAnnotation {} ")
                     .test(aClass ->
                           {
                              List<Parameter> parameters = aClass.getMethods()
                                                                 .get(0)
                                                                 .getParameters();

                              assertEquals("@MyAnnotation Long foo", render(DEFAULT, parameters.get(0)).declaration());
                              assertEquals("Long... foo2", render(DEFAULT, parameters.get(1)).declaration());
                           },
                           aClass ->
                           {
                              List<Parameter> parameters = aClass.getMethods()
                                                                 .get(0)
                                                                 .getParameters();

                              assertEquals("@MyAnnotation Long arg0", render(DEFAULT, parameters.get(0)).declaration());
                              assertEquals("Long... arg1", render(DEFAULT, parameters.get(1)).declaration());
                           });
   }
}