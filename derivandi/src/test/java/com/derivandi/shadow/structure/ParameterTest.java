package com.derivandi.shadow.structure;

import com.derivandi.api.Ap;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.derivandi.api.test.ProcessorTest.processorTest;
import static org.junit.jupiter.api.Assertions.*;

class ParameterTest
{
   @Test
   void getSurrounding()
   {
      processorTest().withCodeToCompile("ParameterExample.java", """
                                                                 public class ParameterExample
                                                                 {
                                                                    public ParameterExample(String name) {}
                                                                 
                                                                    public void foo(Long foo) { }
                                                                 }
                                                                 """)
                     .process(context ->
                              {
                                 Ap.Class example = context.getClassOrThrow("ParameterExample");
                                 Ap.Method method = example.getMethods("foo").get(0);
                                 Ap.Parameter foo = method.getParameterOrThrow("foo");
                                 assertEquals(method, foo.getSurrounding());

                                 Ap.Constructor constructor = example.getConstructors().get(0);
                                 Ap.Parameter name = constructor.getParameters().get(0);
                                 assertEquals(constructor, name.getSurrounding());
                              });
   }

   @Test
   void isVarArgs()
   {
      processorTest().withCodeToCompile("VarArgsExample.java",
                                        """
                                        class VarArgsExample{
                                            VarArgsExample(String s, String... s1) {}
                                        }
                                        """)
                     .process(context ->
                              {
                                 Ap.Class example = context.getClassOrThrow("VarArgsExample");
                                 Ap.Constructor constructor = example.getConstructors().get(0);
                                 List<Ap.Parameter> parameters = constructor.getParameters();
                                 assertFalse(parameters.get(0).isVarArgs());
                                 assertTrue(parameters.get(1).isVarArgs());
                              });
   }
}
