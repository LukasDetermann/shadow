package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class VariableTest<SURROUNDING extends Shadow, VARIABLE extends Variable<SURROUNDING>> extends ShadowTest<VARIABLE>
{
   VariableTest(Function<AnnotationProcessingContext, VARIABLE> variableSupplier)
   {
      super(variableSupplier);
   }

   @Test
   void testIsSubtypeOf()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Parameter parameter = shadowApi.getClassOrThrow("ParameterExample")
                                                              .getConstructors().get(0)
                                                              .getParameters().get(0);
                               assertTrue(parameter.isSubtypeOf(shadowApi.getClassOrThrow("java.lang.String")));
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
   void testIsAssignableFrom()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Field field = shadowApi.getClassOrThrow("FieldExample")
                                                      .getFields().get(0);
                               assertTrue(field.isAssignableFrom(shadowApi.getClassOrThrow("java.lang.Integer")));
                            })
                   .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                   .compile();
   }

   @Test
   void testGetPackage()
   {
      ProcessorTest.process(shadowApi -> assertEquals(shadowApi.getPackages("io.determann.shadow.example.processed.test.field").get(0),
                                                      shadowApi.getClassOrThrow("io.determann.shadow.example.processed.test.field.FieldExample")
                                                               .getFields().get(0)
                                                               .getPackage()))
                   .withCodeToCompile("FieldExample.java", """
                         package io.determann.shadow.example.processed.test.field;

                         public class FieldExample
                         {
                            public static final int ID = 2;
                         }
                         """)
                   .compile();
   }
}
