package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Constructor;
import org.determann.shadow.api.shadow.Executable;
import org.determann.shadow.api.shadow.Method;
import org.determann.shadow.api.shadow.Parameter;
import org.junit.jupiter.api.Test;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParameterTest extends VariableTest<Executable, Parameter>
{
   protected ParameterTest()
   {
      super(() -> SHADOW_API.getClass("org.determann.shadow.example.processed.test.parameter.ParameterExample")
                            .getMethods("foo")
                            .get(0)
                            .getParameter("foo"));
   }

   @Test
   void testGetSurrounding()
   {
      Method method = SHADOW_API.getClass("org.determann.shadow.example.processed.test.parameter.ParameterExample")
                                .getMethods("foo")
                                .get(0);

      Parameter methodParameter = method.getParameter("foo");

      assertEquals(method, methodParameter.getSurrounding());

      Constructor constructor = SHADOW_API.getClass("org.determann.shadow.example.processed.test.parameter.ParameterExample")
                                          .getConstructors()
                                          .get(0);
      Parameter constructorParameter = constructor.getParameters().get(0);
      assertEquals(constructor, constructorParameter.getSurrounding());
   }
}
