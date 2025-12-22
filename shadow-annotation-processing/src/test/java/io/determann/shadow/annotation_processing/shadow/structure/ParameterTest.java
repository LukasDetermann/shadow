package io.determann.shadow.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParameterTest
{
   @Test
   void getSurrounding()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class example = context.getClassOrThrow("ParameterExample");
                               Ap.Method method = example.getMethods("foo").get(0);
                               Ap.Parameter foo = method.getParameterOrThrow("foo");
                               assertEquals(method, foo.getSurrounding());

                               Ap.Constructor constructor = example.getConstructors().get(0);
                               Ap.Parameter name = constructor.getParameters().get(0);
                               assertEquals(constructor, name.getSurrounding());
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
                               Ap.Class example = context.getClassOrThrow("VarArgsExample");
                               Ap.Constructor constructor = example.getConstructors().get(0);
                               List<Ap.Parameter> parameters = constructor.getParameters();
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
