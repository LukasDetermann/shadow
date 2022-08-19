package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.ElementBacked;
import org.determann.shadow.api.NestingKind;
import org.determann.shadow.api.shadow.Declared;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class DeclaredTest<DECLARED extends Declared> extends ShadowTest<DECLARED>
{
   protected DeclaredTest(Supplier<DECLARED> shadowSupplier)
   {
      super(shadowSupplier);
   }

   abstract void testisSubtypeOf();

   @Test
   void testGetFormalGenerics()
   {
      assertEquals("T",
                   SHADOW_API.getInterface("java.lang.Comparable")
                             .getFormalGenerics()
                             .stream()
                             .map(Object::toString)
                             .collect(Collectors.joining()));
   }

   @Test
   void testGetNesting()
   {
      assertEquals(NestingKind.OUTER,
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.declared.NestingExample").getNesting());
      assertEquals(NestingKind.INNER,
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.declared.NestingExample.Inner").getNesting());
   }

   @Test
   void testGetField()
   {
      assertEquals(2.7182818284590452354D, SHADOW_API.getClass("java.lang.Math").getField("E").getConstantValue());
      assertThrows(NoSuchElementException.class, () -> SHADOW_API.getClass("java.lang.Math").getField("EEEE"));
   }

   @Test
   void testGetFields()
   {
      assertEquals(List.of("E",
                           "PI",
                           "DEGREES_TO_RADIANS",
                           "RADIANS_TO_DEGREES",
                           "negativeZeroFloatBits",
                           "negativeZeroDoubleBits",
                           "twoToTheDoubleScaleUp",
                           "twoToTheDoubleScaleDown"),
                   SHADOW_API.getClass("java.lang.Math")
                             .getFields()
                             .stream()
                             .map(ElementBacked::getSimpleName)
                             .toList());
   }

   @Test
   void testGetMethods()
   {
      assertEquals(List.of("wait()",
                           "wait(long)",
                           "wait(long,int)"),
                   SHADOW_API.getClass("java.lang.Object").getMethods("wait")
                             .stream()
                             .map(Object::toString)
                             .toList());

      assertEquals(0, SHADOW_API.getClass("java.lang.Object").getMethods("asdf").size());

      assertEquals(
            List.of("getClass()",
                    "hashCode()",
                    "equals(java.lang.Object)",
                    "clone()",
                    "toString()",
                    "notify()",
                    "notifyAll()",
                    "wait()",
                    "wait(long)",
                    "wait(long,int)",
                    "finalize()"),
            SHADOW_API.getClass("java.lang.Object").getMethods().stream().map(Object::toString).toList());
   }

   @Test
   void testGetConstructors()
   {
      assertEquals(List.of("Object()"),
                   SHADOW_API.getClass("java.lang.Object").getConstructors().stream()
                             .map(Object::toString)
                             .toList());
      assertEquals(List.of("Math()"),
                   SHADOW_API.getClass("java.lang.Math").getConstructors().stream()
                             .map(Object::toString)
                             .toList());
   }

   abstract void testGetDirectSuperTypes();

   abstract void testGetSuperTypes();

   @Test
   void testGetPackage()
   {
      assertEquals("java.lang",
                   SHADOW_API.getClass("java.lang.Object").getPackage().toString());
   }

   @Test
   void testGetBinaryName()
   {
      //Outer
      assertEquals("java.lang.Object", SHADOW_API.getClass("java.lang.Object").getBinaryName());
      //Inner
      assertEquals("java.lang.Math$RandomNumberGeneratorHolder",
                   SHADOW_API.getClass("java.lang.Math.RandomNumberGeneratorHolder").getBinaryName());
   }
}
