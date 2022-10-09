package org.determann.shadow.api.shadow;

import org.determann.shadow.api.converter.ShadowConverter;
import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InterfaceTest extends DeclaredTest<Interface>
{
   InterfaceTest()
   {
      super(shadowApi -> shadowApi.getInterface("java.util.function.Function"));
   }

   @Test
   void testGetDirectInterfaces()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(0, shadowApi.getInterface("java.util.function.Function").getDirectInterfaces().size());

                                 assertEquals(List.of("java.util.function.Function"),
                                              shadowApi.getInterface("java.util.function.UnaryOperator")
                                                       .getDirectInterfaces()
                                                       .stream()
                                                       .map(Object::toString)
                                                       .toList());
                              })
                     .compile();
   }

   @Test
   void testIsFunctional()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertTrue(shadowApi.getInterface("java.util.function.Function").isFunctional());
                                 assertTrue(shadowApi.getInterface("java.lang.Comparable").isFunctional());
                                 assertFalse(shadowApi.getInterface("java.util.List").isFunctional());
                              })
                     .compile();
   }

   @Test
   void testWithGenerics()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertThrows(IllegalArgumentException.class,
                                              () -> shadowApi.getInterface("InterpolateGenericsExample.DependentGeneric")
                                                             .withGenerics("java.lang.String"));

                                 assertThrows(IllegalArgumentException.class,
                                              () -> shadowApi.getInterface("java.io.Serializable").withGenerics("java.io.Serializable"));

                                 assertEquals(List.of(shadowApi.getClass("java.lang.String")),
                                              shadowApi.getInterface("InterpolateGenericsExample.IndependentGeneric")
                                                       .withGenerics("java.lang.String")
                                                       .getGenerics());

                                 assertEquals(List.of(shadowApi.getClass("java.lang.String"), shadowApi.getClass("java.lang.Number")),
                                              shadowApi.getInterface("InterpolateGenericsExample.DependentGeneric")
                                                       .withGenerics("java.lang.String", "java.lang.Number")
                                                       .getGenerics());
                              })
                     .withCodeToCompile("InterpolateGenericsExample.java", """
                           public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {
                              interface IndependentGeneric<C> {}
                              interface DependentGeneric<D extends E, E> {}
                           }
                           """)
                     .compile();
   }

   @Test
   void testInterpolateGenerics()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 Interface declared = shadowApi.getInterface("InterpolateGenericsExample")
                                                               .withGenerics(shadowApi.getClass("java.lang.String"),
                                                                             shadowApi.getConstants().getUnboundWildcard());
                                 Interface capture = declared.interpolateGenerics();
                                 Shadow<TypeMirror> interpolated = shadowApi.convert(capture.getGenerics().get(1))
                                                                            .toOptionalGeneric()
                                                                            .map(Generic::getExtends)
                                                                            .map(shadowApi::convert)
                                                                            .flatMap(ShadowConverter::toOptionalInterface)
                                                                            .map(Interface::getGenerics)
                                                                            .map(shadows -> shadows.get(0))
                                                                            .orElseThrow();
                                 assertEquals(shadowApi.getClass("java.lang.String"), interpolated);

                                 Interface independentExample = shadowApi.getInterface("InterpolateGenericsExample.IndependentGeneric")
                                                                         .withGenerics(shadowApi.getConstants().getUnboundWildcard());
                                 Interface independentCapture = independentExample.interpolateGenerics();
                                 Shadow<TypeMirror> interpolatedIndependent = shadowApi.convert(independentCapture.getGenerics().get(0))
                                                                                       .toOptionalGeneric()
                                                                                       .map(Generic::getExtends)
                                                                                       .orElseThrow();
                                 assertEquals(shadowApi.getClass("java.lang.Object"), interpolatedIndependent);

                                 Interface dependentExample = shadowApi.getInterface("InterpolateGenericsExample.DependentGeneric")
                                                                       .withGenerics(shadowApi.getConstants().getUnboundWildcard(),
                                                                                     shadowApi.getClass("java.lang.String"));
                                 Interface dependentCapture = dependentExample.interpolateGenerics();
                                 Shadow<TypeMirror> interpolatedDependent = shadowApi.convert(dependentCapture.getGenerics().get(0))
                                                                                     .toOptionalGeneric()
                                                                                     .map(Generic::getExtends)
                                                                                     .orElseThrow();
                                 assertEquals(shadowApi.getClass("java.lang.String"), interpolatedDependent);
                              })
                     .withCodeToCompile("InterpolateGenericsExample.java", """
                           public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {
                              interface IndependentGeneric<C> {}
                              interface DependentGeneric<D extends E, E> {}
                           }
                           """)
                     .compile();
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertTrue(getShadowSupplier().apply(shadowApi).isSubtypeOf(shadowApi.getClass("java.lang.Object")));
                                 assertTrue(getShadowSupplier().apply(shadowApi).isSubtypeOf(getShadowSupplier().apply(shadowApi)));
                                 assertFalse(getShadowSupplier().apply(shadowApi).isSubtypeOf(shadowApi.getClass("java.lang.Number")));
                              })
                     .compile();
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(List.of(shadowApi.getClass("java.lang.Object")),
                                              shadowApi.getInterface("DirektSuperTypeExample.InterfaceNoParent")
                                                       .getDirectSuperTypes());

                                 assertEquals(List.of(shadowApi.getClass("java.lang.Object"),
                                                      shadowApi.getInterface("java.lang.Comparable"),
                                                      shadowApi.getInterface("java.util.function.Consumer")),
                                              shadowApi.getInterface("DirektSuperTypeExample.InterfaceParent")
                                                       .getDirectSuperTypes());
                              })
                     .withCodeToCompile("DirektSuperTypeExample.java", """
                           import java.util.function.Consumer;
                           import java.util.function.Supplier;

                           public class DirektSuperTypeExample {
                              interface InterfaceNoParent {}

                              interface InterfaceParent extends Comparable<InterfaceParent>,
                                                                Consumer<InterfaceParent> {}
                           }
                           """)
                     .compile();
   }

   @Test
   @Override
   void testGetSuperTypes()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(Set.of(shadowApi.getClass("java.lang.Object")),
                                              shadowApi.getInterface("DirektSuperTypeExample.InterfaceNoParent")
                                                       .getSuperTypes());

                                 assertEquals(Set.of(shadowApi.getClass("java.lang.Object"),
                                                     shadowApi.getInterface("java.lang.Comparable"),
                                                     shadowApi.getInterface("java.util.function.Consumer")),
                                              shadowApi.getInterface("DirektSuperTypeExample.InterfaceParent")
                                                       .getSuperTypes());
                              })
                     .withCodeToCompile("DirektSuperTypeExample.java", """
                           import java.util.function.Consumer;
                           import java.util.function.Supplier;

                           public class DirektSuperTypeExample {
                              interface InterfaceNoParent {}

                              interface InterfaceParent extends Comparable<InterfaceParent>,
                                                                Consumer<InterfaceParent> {}
                           }
                           """)
                     .compile();
   }
}
