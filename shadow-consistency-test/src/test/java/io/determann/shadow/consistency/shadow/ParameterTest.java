package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Constructor;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Method;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Parameter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParameterTest extends VariableTest<LM_Parameter>
{
   @Test
   void testGetSurrounding()
   {
      ProcessorTest.process(context ->
                            {
                               LM_Method method = context.getClassOrThrow("ParameterExample")
                                                         .getMethods("foo")
                                                         .get(0);

                               LM_Parameter methodParameter = method.getParameterOrThrow("foo");

                               assertEquals(method, methodParameter.getSurrounding());

                               LM_Constructor constructor = context.getClassOrThrow("ParameterExample")
                                                                   .getConstructors()
                                                                   .get(0);
                               LM_Parameter constructorParameter = constructor.getParameters().get(0);
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
      ProcessorTest.process(context ->
                            {
                               List<LM_Parameter> parameters = context.getClassOrThrow("VarArgsExample").getConstructors()
                                                                      .get(0)
                                                                      .getParameters();

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
