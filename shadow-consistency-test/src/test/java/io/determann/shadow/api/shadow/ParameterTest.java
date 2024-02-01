package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
      ProcessorTest.process(shadowApi ->
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

   @Test
   void isVarArgs()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               List<Parameter> parameters = shadowApi.getClassOrThrow("VarArgsExample").getConstructors().get(0).getParameters();

                               assertFalse(parameters.get(0).isVarArgs());
                               assertTrue(parameters.get(1).isVarArgs());
                            })
                   .withCodeToCompile("VarArgsExample.java",
                                      """
                                            class VarArgsExample{
                                                VarArgsExample(String s, String... s1) {}
                                            }
                                            """)
                   .compile();
   }
}
