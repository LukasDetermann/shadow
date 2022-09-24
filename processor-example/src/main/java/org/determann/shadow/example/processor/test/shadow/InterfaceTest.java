package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.converter.ShadowConverter;
import org.determann.shadow.api.shadow.Generic;
import org.determann.shadow.api.shadow.Interface;
import org.determann.shadow.api.shadow.Shadow;
import org.junit.jupiter.api.Test;

import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Set;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.*;

public class InterfaceTest extends DeclaredTest<Interface>
{
   protected InterfaceTest()
   {
      super(() -> SHADOW_API.getInterface("java.util.function.Function"));
   }

   @Test
   void testGetDirectInterfaces()
   {
      assertEquals(0, SHADOW_API.getInterface("java.util.function.Function").getDirectInterfaces().size());

      assertEquals(List.of("java.util.function.Function"),
                   SHADOW_API.getInterface("java.util.function.UnaryOperator").getDirectInterfaces().stream().map(Object::toString).toList());
   }

   @Test
   void testIsFunctional()
   {
      assertTrue(SHADOW_API.getInterface("java.util.function.Function").isFunctional());
      assertTrue(SHADOW_API.getInterface("java.lang.Comparable").isFunctional());
      assertFalse(SHADOW_API.getInterface("java.util.List").isFunctional());
   }

   @Test
   void testWithGenerics()
   {
      assertThrows(IllegalArgumentException.class,
                   () -> SHADOW_API.getInterface(
                                         "org.determann.shadow.example.processed.test.interfase.InterpolateGenericsExample.DependentGeneric")
                                   .withGenerics("java.lang.String"));

      assertThrows(IllegalArgumentException.class, () -> SHADOW_API.getInterface("java.io.Serializable").withGenerics("java.io.Serializable"));

      assertEquals(List.of(SHADOW_API.getClass("java.lang.String")),
                   SHADOW_API.getInterface("org.determann.shadow.example.processed.test.interfase.InterpolateGenericsExample.IndependentGeneric")
                             .withGenerics("java.lang.String")
                             .getGenerics());

      assertEquals(List.of(SHADOW_API.getClass("java.lang.String"), SHADOW_API.getClass("java.lang.Number")),
                   SHADOW_API.getInterface("org.determann.shadow.example.processed.test.interfase.InterpolateGenericsExample.DependentGeneric")
                             .withGenerics("java.lang.String", "java.lang.Number")
                             .getGenerics());
   }

   @Test
   void testInterpolateGenerics()
   {
      Interface declared = SHADOW_API.getInterface("org.determann.shadow.example.processed.test.interfase.InterpolateGenericsExample")
                                     .withGenerics(SHADOW_API.getClass("java.lang.String"),
                                                   SHADOW_API.getConstants().getUnboundWildcard());
      Interface capture = declared.interpolateGenerics();
      Shadow<TypeMirror> interpolated = SHADOW_API.convert(capture.getGenerics().get(1))
                                                  .toOptionalGeneric()
                                                  .map(Generic::getExtends)
                                                  .map(SHADOW_API::convert)
                                                  .flatMap(ShadowConverter::toOptionalInterface)
                                                  .map(Interface::getGenerics)
                                                  .map(shadows -> shadows.get(0))
                                                  .orElseThrow();
      assertEquals(SHADOW_API.getClass("java.lang.String"), interpolated);

      Interface independentExample = SHADOW_API.getInterface(
                                                     "org.determann.shadow.example.processed.test.interfase.InterpolateGenericsExample.IndependentGeneric")
                                               .withGenerics(SHADOW_API.getConstants().getUnboundWildcard());
      Interface independentCapture = independentExample.interpolateGenerics();
      Shadow<TypeMirror> interpolatedIndependent = SHADOW_API.convert(independentCapture.getGenerics().get(0))
                                                             .toOptionalGeneric()
                                                             .map(Generic::getExtends)
                                                             .orElseThrow();
      assertEquals(SHADOW_API.getClass("java.lang.Object"), interpolatedIndependent);

      Interface dependentExample = SHADOW_API.getInterface(
                                                   "org.determann.shadow.example.processed.test.interfase.InterpolateGenericsExample.DependentGeneric")
                                             .withGenerics(SHADOW_API.getConstants().getUnboundWildcard(),
                                                           SHADOW_API.getClass("java.lang.String"));
      Interface dependentCapture = dependentExample.interpolateGenerics();
      Shadow<TypeMirror> interpolatedDependent = SHADOW_API.convert(dependentCapture.getGenerics().get(0))
                                                           .toOptionalGeneric()
                                                           .map(Generic::getExtends)
                                                           .orElseThrow();
      assertEquals(SHADOW_API.getClass("java.lang.String"), interpolatedDependent);
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      assertTrue(getShadowSupplier().get().isSubtypeOf(SHADOW_API.getClass("java.lang.Object")));
      assertTrue(getShadowSupplier().get().isSubtypeOf(getShadowSupplier().get()));
      assertFalse(getShadowSupplier().get().isSubtypeOf(SHADOW_API.getClass("java.lang.Number")));
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      assertEquals(List.of(SHADOW_API.getClass("java.lang.Object")),
                   SHADOW_API.getInterface("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.InterfaceNoParent")
                             .getDirectSuperTypes());

      assertEquals(List.of(SHADOW_API.getClass("java.lang.Object"),
                           SHADOW_API.getInterface("java.lang.Comparable"),
                           SHADOW_API.getInterface("java.util.function.Consumer")),
                   SHADOW_API.getInterface("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.InterfaceParent")
                             .getDirectSuperTypes());
   }

   @Test
   @Override
   void testGetSuperTypes()
   {
      assertEquals(Set.of(SHADOW_API.getClass("java.lang.Object")),
                   SHADOW_API.getInterface("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.InterfaceNoParent")
                             .getSuperTypes());

      assertEquals(Set.of(SHADOW_API.getClass("java.lang.Object"),
                          SHADOW_API.getInterface("java.lang.Comparable"),
                          SHADOW_API.getInterface("java.util.function.Consumer")),
                   SHADOW_API.getInterface("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.InterfaceParent")
                             .getSuperTypes());
   }
}
