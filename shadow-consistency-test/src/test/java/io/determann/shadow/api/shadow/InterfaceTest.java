package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.converter.ShadowConverter;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.converter.Converter.convert;
import static org.junit.jupiter.api.Assertions.*;

class InterfaceTest extends DeclaredTest<Interface>
{
   InterfaceTest()
   {
      super(shadowApi -> shadowApi.getInterfaceOrThrow("java.util.function.Function"));
   }

   @Test
   void testGetDirectInterfaces()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(0, shadowApi.getInterfaceOrThrow("java.util.function.Function").getDirectInterfaces().size());

                               assertEquals(List.of("java.util.function.Function"),
                                            shadowApi.getInterfaceOrThrow("java.util.function.UnaryOperator")
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
      ProcessorTest.process(shadowApi ->
                            {
                               assertTrue(shadowApi.getInterfaceOrThrow("java.util.function.Function").isFunctional());
                               assertTrue(shadowApi.getInterfaceOrThrow("java.lang.Comparable").isFunctional());
                               assertFalse(shadowApi.getInterfaceOrThrow("java.util.List").isFunctional());
                            })
                   .compile();
   }

   @Test
   void testWithGenerics()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertThrows(IllegalArgumentException.class,
                                            () -> shadowApi.withGenerics(shadowApi.getInterfaceOrThrow(
                                                  "InterpolateGenericsExample.DependentGeneric"), "java.lang.String"));

                               assertThrows(IllegalArgumentException.class,
                                            () -> shadowApi.withGenerics(shadowApi.getInterfaceOrThrow("java.io.Serializable"),
                                                                         "java.io.Serializable"));

                               assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.String")),
                                            shadowApi.withGenerics(shadowApi.getInterfaceOrThrow("InterpolateGenericsExample.IndependentGeneric"),
                                                                   "java.lang.String")
                                                     .getGenericTypes());

                               assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.String"),
                                                    shadowApi.getClassOrThrow("java.lang.Number")),
                                            shadowApi.withGenerics(shadowApi.getInterfaceOrThrow("InterpolateGenericsExample.DependentGeneric"),
                                                                   "java.lang.String",
                                                                   "java.lang.Number")
                                                     .getGenericTypes());
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
      ProcessorTest.process(shadowApi ->
                            {
                               Interface declared = shadowApi.withGenerics(shadowApi.getInterfaceOrThrow("InterpolateGenericsExample"),
                                                                           shadowApi.getClassOrThrow("java.lang.String"),
                                                                           shadowApi.getConstants().getUnboundWildcard());
                               Interface capture = shadowApi.interpolateGenerics(declared);
                               Shadow interpolated = convert(capture.getGenericTypes().get(1))
                                     .toGeneric()
                                     .map(Generic::getExtends)
                                     .map(Converter::convert)
                                     .flatMap(ShadowConverter::toInterface)
                                     .map(Interface::getGenericTypes)
                                     .map(shadows -> shadows.get(0))
                                     .orElseThrow();
                               assertEquals(shadowApi.getClassOrThrow("java.lang.String"), interpolated);

                               Interface independentExample = shadowApi.withGenerics(shadowApi.getInterfaceOrThrow(
                                                                                           "InterpolateGenericsExample.IndependentGeneric"),
                                                                                     shadowApi.getConstants().getUnboundWildcard());
                               Interface independentCapture = shadowApi.interpolateGenerics(independentExample);
                               Shadow interpolatedIndependent = convert(independentCapture.getGenericTypes().get(0))
                                     .toGeneric()
                                     .map(Generic::getExtends)
                                     .orElseThrow();
                               assertEquals(shadowApi.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               Interface dependentExample = shadowApi.withGenerics(shadowApi.getInterfaceOrThrow(
                                                                                         "InterpolateGenericsExample.DependentGeneric"), shadowApi.getConstants().getUnboundWildcard(),
                                                                                   shadowApi.getClassOrThrow("java.lang.String"));
                               Interface dependentCapture = shadowApi.interpolateGenerics(dependentExample);
                               Shadow interpolatedDependent = convert(dependentCapture.getGenericTypes().get(0))
                                     .toGeneric()
                                     .map(Generic::getExtends)
                                     .orElseThrow();
                               assertEquals(shadowApi.getClassOrThrow("java.lang.String"), interpolatedDependent);
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
      ProcessorTest.process(shadowApi ->
                            {
                               assertTrue(getShadowSupplier().apply(shadowApi).isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Object")));
                               assertTrue(getShadowSupplier().apply(shadowApi).isSubtypeOf(getShadowSupplier().apply(shadowApi)));
                               assertFalse(getShadowSupplier().apply(shadowApi).isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Number")));
                            })
                   .compile();
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.Object")),
                                            shadowApi.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceNoParent")
                                                     .getDirectSuperTypes());

                               assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.Object"),
                                                    shadowApi.getInterfaceOrThrow("java.lang.Comparable"),
                                                    shadowApi.getInterfaceOrThrow("java.util.function.Consumer")),
                                            shadowApi.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceParent")
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
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(Set.of(shadowApi.getClassOrThrow("java.lang.Object")),
                                            shadowApi.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceNoParent")
                                                     .getSuperTypes());

                               assertEquals(Set.of(shadowApi.getClassOrThrow("java.lang.Object"),
                                                   shadowApi.getInterfaceOrThrow("java.lang.Comparable"),
                                                   shadowApi.getInterfaceOrThrow("java.util.function.Consumer")),
                                            shadowApi.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceParent")
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
