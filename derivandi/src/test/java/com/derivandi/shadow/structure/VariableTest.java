package com.derivandi.shadow.structure;

import com.derivandi.api.Ap;
import org.junit.jupiter.api.Test;

import static com.derivandi.api.test.ProcessorTest.processorTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class VariableTest
{
   @Test
   void isSubtypeOf()
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
                                 Ap.Class string = context.getClassOrThrow("java.lang.String");
                                 Ap.Class example = context.getClassOrThrow("ParameterExample");
                                 Ap.Constructor constructor = example.getConstructors().get(0);
                                 Ap.Parameter parameter = constructor.getParameters().get(0);
                                 assertTrue(parameter.isSubtypeOf(string));
                              });
   }

   @Test
   void isAssignableFrom()
   {
      processorTest().withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                     .process(context ->
                              {
                                 Ap.Class integer = context.getClassOrThrow("java.lang.Integer");
                                 Ap.Class example = context.getClassOrThrow("FieldExample");
                                 Ap.Field field = example.getFields().get(0);
                                 assertTrue(field.isAssignableFrom(integer));
                              });
   }
}
