package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.Parameter;
import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.ShadowApi.render;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParameterRendererTest
{
   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               List<Parameter> parameters = shadowApi.getInterfaceOrThrow("ParameterExample").getMethods().get(0).getParameters();
                               assertEquals("@MyAnnotation Long foo", render(parameters.get(0)).declaration());
                               assertEquals("Long... foo2", render(parameters.get(1)).declaration());
                            })
                   .withCodeToCompile("ParameterExample.java", """
                         public interface ParameterExample {
                            void foo(@MyAnnotation Long foo, Long... foo2);
                         }
                         """)
                   .withCodeToCompile("MyAnnotation.java", "@interface MyAnnotation {} ")
                   .compile();
   }
}