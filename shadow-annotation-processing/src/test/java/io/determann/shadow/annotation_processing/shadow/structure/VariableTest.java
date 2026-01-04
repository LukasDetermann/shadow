package io.determann.shadow.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class VariableTest
{
   @Test
   void isSubtypeOf()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class string = context.getClassOrThrow("java.lang.String");
                               Ap.Class example = context.getClassOrThrow("ParameterExample");
                               Ap.Constructor constructor = example.getConstructors().get(0);
                               Ap.Parameter parameter = constructor.getParameters().get(0);
                               assertTrue(parameter.isSubtypeOf(string));
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
   void isAssignableFrom()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class integer = context.getClassOrThrow("java.lang.Integer");
                               Ap.Class example = context.getClassOrThrow("FieldExample");
                               Ap.Field field = example.getFields().get(0);
                               assertTrue(field.isAssignableFrom(integer));
                            })
                   .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                   .compile();
   }
}
