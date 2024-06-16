package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class VariableTest<VARIABLE extends Variable> extends ShadowTest<VARIABLE>
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
                               Parameter parameter = query(query(shadowApi.getClassOrThrow("ParameterExample"))
                                     .getConstructors().get(0))
                                     .getParameters().get(0);
                               assertTrue(query(parameter).isSubtypeOf(shadowApi.getClassOrThrow("java.lang.String")));
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
                               Field field = query(shadowApi.getClassOrThrow("FieldExample"))
                                     .getFields().get(0);
                               assertTrue(query(field).isAssignableFrom(shadowApi.getClassOrThrow("java.lang.Integer")));
                            })
                   .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                   .compile();
   }

   @Test
   void testGetPackage()
   {
      ProcessorTest.process(shadowApi -> assertEquals(shadowApi.getPackages("io.determann.shadow.example.processed.test.field").get(0),
                                                      query(query(shadowApi.getClassOrThrow("io.determann.shadow.example.processed.test.field.FieldExample"))
                                                            .getFields().get(0))
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
