package org.determann.shadow.api.shadow;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import javax.lang.model.type.TypeMirror;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class VariableTest<SURROUNDING extends Shadow<? extends TypeMirror>, VARIABLE extends Variable<SURROUNDING>>
      extends ShadowTest<VARIABLE>
{
   VariableTest(Function<ShadowApi, VARIABLE> variableSupplier)
   {
      super(variableSupplier);
   }

   @Test
   void testIsSubtypeOf()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 Parameter parameter = shadowApi.getClassOrThrow("ParameterExample")
                                                                .getConstructors().get(0)
                                                                .getParameters().get(0);
                                 assertTrue(parameter.isSubtypeOf(shadowApi.getClassOrThrow("java.lang.String")));
                              })
                     .withCodeToCompile("ParameterExample.java", "              public class ParameterExample\n" +
                                                                 "                           {\n" +
                                                                 "                              public ParameterExample(String name) {}\n" +
                                                                 "\n" +
                                                                 "                              public void foo(Long foo) { }\n" +
                                                                 "                           }")
                     .compile();
   }

   @Test
   void testIsAssignableFrom()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 Field field = shadowApi.getClassOrThrow("FieldExample")
                                                        .getFields().get(0);
                                 assertTrue(field.isAssignableFrom(shadowApi.getClassOrThrow("java.lang.Integer")));
                              })
                     .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                     .compile();
   }
}
