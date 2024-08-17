package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.structure.FieldLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.ParameterLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.VariableLangModel;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class VariableTest<VARIABLE extends VariableLangModel> extends ShadowTest<VARIABLE>
{
   VariableTest(Function<AnnotationProcessingContext, VARIABLE> variableSupplier)
   {
      super(variableSupplier);
   }

   @Test
   void testIsSubtypeOf()
   {
      ProcessorTest.process(context ->
                            {
                               ParameterLangModel parameter = context.getClassOrThrow("ParameterExample")
                                                                     .getConstructors().get(0)
                                                                     .getParameters().get(0);
                               assertTrue(parameter.isSubtypeOf(context.getClassOrThrow("java.lang.String")));
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
      ProcessorTest.process(context ->
                            {
                               FieldLangModel field = context.getClassOrThrow("FieldExample")
                                                             .getFields().get(0);
                               assertTrue(field.isAssignableFrom(context.getClassOrThrow("java.lang.Integer")));
                            })
                   .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                   .compile();
   }

   @Test
   void testGetPackage()
   {
      ProcessorTest.process(context -> assertEquals(context.getPackages("io.determann.shadow.example.processed.test.field").get(0),
                                                      context.getClassOrThrow("io.determann.shadow.example.processed.test.field.FieldExample")
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
