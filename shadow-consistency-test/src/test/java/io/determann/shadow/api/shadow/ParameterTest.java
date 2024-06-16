package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static org.junit.jupiter.api.Assertions.*;

class ParameterTest extends VariableTest<Parameter>
{
   ParameterTest()
   {
      super(shadowApi -> query(query(shadowApi.getClassOrThrow("ParameterExample"))
            .getMethods("foo")
            .get(0))
            .getParameterOrThrow("foo"));
   }

   @Test
   void testGetSurrounding()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Method method = query(shadowApi.getClassOrThrow("ParameterExample"))
                                     .getMethods("foo")
                                     .get(0);

                               Parameter methodParameter = query(method).getParameterOrThrow("foo");

                               assertEquals(method, query(methodParameter).getSurrounding());

                               Constructor constructor = query(shadowApi.getClassOrThrow("ParameterExample"))
                                     .getConstructors()
                                     .get(0);
                               Parameter constructorParameter = query(constructor).getParameters().get(0);
                               assertEquals(constructor, query(constructorParameter).getSurrounding());
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
                               List<Parameter> parameters = query(query(shadowApi.getClassOrThrow("VarArgsExample")).getConstructors()
                                                                                                              .get(0))
                                                                                                              .getParameters();

                               assertFalse(query(parameters.get(0)).isVarArgs());
                               assertTrue(query(parameters.get(1)).isVarArgs());
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
