package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Field;
import org.determann.shadow.api.shadow.Parameter;
import org.determann.shadow.api.shadow.Variable;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VariableTest<RETURN, VARIABLE extends Variable<RETURN>> extends ShadowTest<VARIABLE>
{
   protected VariableTest(Supplier<VARIABLE> variableSupplier)
   {
      super(variableSupplier);
   }

   @Test
   void testIsSubtypeOf()
   {
      Parameter parameter = SHADOW_API.getClass("org.determann.shadow.example.processed.test.parameter.ParameterExample")
                                      .getConstructors().get(0)
                                      .getParameters().get(0);
      assertTrue(parameter.isSubtypeOf(SHADOW_API.getClass("java.lang.String")));
   }

   @Test
   void testIsAssignableFrom()
   {
      Field field = SHADOW_API.getClass("org.determann.shadow.example.processed.test.field.FieldExample")
                              .getFields().get(0);
      assertTrue(field.isAssignableFrom(SHADOW_API.getClass("java.lang.Integer")));
   }

   @Test
   void testGetPackage()
   {
      assertEquals(SHADOW_API.getPackages("org.determann.shadow.example.processed.test.field").get(0),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.field.FieldExample")
                             .getFields().get(0)
                             .getPackage());
   }
}
