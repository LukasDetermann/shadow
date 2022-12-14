package org.determann.shadow.api.shadow;

import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParameterTest extends VariableTest<Executable, Parameter>
{
   ParameterTest()
   {
      super(shadowApi -> shadowApi.getClassOrThrow("ParameterExample")
                                  .getMethods("foo")
                                  .get(0)
                                  .getParameterOrThrow("foo"));
   }

   @Test
   void testGetSurrounding()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 Method method = shadowApi.getClassOrThrow("ParameterExample")
                                                          .getMethods("foo")
                                                          .get(0);

                                 Parameter methodParameter = method.getParameterOrThrow("foo");

                                 assertEquals(method, methodParameter.getSurrounding());

                                 Constructor constructor = shadowApi.getClassOrThrow("ParameterExample")
                                                                    .getConstructors()
                                                                    .get(0);
                                 Parameter constructorParameter = constructor.getParameters().get(0);
                                 assertEquals(constructor, constructorParameter.getSurrounding());
                              })
                     .withCodeToCompile("ParameterExample.java", """
                           public class ParameterExample
                           {
                              public ParameterExample(String name) {}

                              public void foo(Long foo) { }
                           }
                           """)
                     .compile();
   }
}
