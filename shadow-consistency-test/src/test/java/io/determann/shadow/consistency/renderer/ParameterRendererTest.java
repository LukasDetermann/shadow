package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.Operations;
import io.determann.shadow.api.lang_model.LM_Queries;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Parameter;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.R_Queries;
import io.determann.shadow.api.reflection.shadow.structure.R_Parameter;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParameterRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.<C_Class>compileTime(context -> context.getClassOrThrow("ReceiverExample"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("ReceiverExample")))
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
                                                  render(DEFAULT, requestOrThrow(aClass, Operations.DECLARED_GET_METHODS).get(0)).declaration()));


      ConsistencyTest.<C_Interface>compileTime(context -> context.getInterfaceOrThrow("ParameterExample"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("ParameterExample")))
                     .withCode("ParameterExample.java", """
                         public interface ParameterExample {
                            void foo(@MyAnnotation Long foo, Long... foo2);
                         }
                         """)
                     .withCode("MyAnnotation.java", "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME) @interface MyAnnotation {} ")
                     .test(aClass ->
                           {
                              List<LM_Parameter> parameters = LM_Queries.query(aClass).getMethods().get(0).getParameters();

                              assertEquals("@MyAnnotation Long foo", render(DEFAULT, parameters.get(0)).declaration());
                              assertEquals("Long... foo2", render(DEFAULT, parameters.get(1)).declaration());
                           },
                           aClass ->
                           {
                              List<R_Parameter> parameters = R_Queries.query(aClass).getMethods()
                                                                      .get(0)
                                                                      .getParameters();

                              assertEquals("@MyAnnotation Long arg0", render(DEFAULT, parameters.get(0)).declaration());
                              assertEquals("Long... arg1", render(DEFAULT, parameters.get(1)).declaration());
                           });
   }
}