package io.determann.shadow.api.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.converter.ShadowConverter;
import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

import static io.determann.shadow.api.ShadowApi.convert;
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

                                 assertEquals(Collections.singletonList(("java.util.function.Function")),
                                              shadowApi.getInterfaceOrThrow("java.util.function.UnaryOperator")
                                                       .getDirectInterfaces()
                                                       .stream()
                                                       .map(Object::toString)
                                                       .collect(Collectors.toList()));
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
                                              () -> shadowApi.getInterfaceOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                             .withGenerics("java.lang.String"));

                                 assertThrows(IllegalArgumentException.class,
                                              () -> shadowApi.getInterfaceOrThrow("java.io.Serializable").withGenerics("java.io.Serializable"));

                                 assertEquals(Arrays.asList(shadowApi.getClassOrThrow("java.lang.String")),
                                              shadowApi.getInterfaceOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                       .withGenerics("java.lang.String")
                                                       .getGenerics());

                                 assertEquals(Arrays.asList(shadowApi.getClassOrThrow("java.lang.String"), shadowApi.getClassOrThrow("java.lang.Number")),
                                              shadowApi.getInterfaceOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                       .withGenerics("java.lang.String", "java.lang.Number")
                                                       .getGenerics());
                              })
                   .withCodeToCompile("InterpolateGenericsExample.java",
                                        "                           public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {\n" +
                                        "                              interface IndependentGeneric<C> {}\n" +
                                        "                              interface DependentGeneric<D extends E, E> {}\n" +
                                        "                           }")
                   .compile();
   }

   @Test
   void testInterpolateGenerics()
   {
      ProcessorTest.process(shadowApi ->
                              {
                                 Interface declared = shadowApi.getInterfaceOrThrow("InterpolateGenericsExample")
                                                               .withGenerics(shadowApi.getClassOrThrow("java.lang.String"),
                                                                             shadowApi.getConstants().getUnboundWildcard());
                                 Interface capture = declared.interpolateGenerics();
                                 Shadow<TypeMirror> interpolated = convert(capture.getGenerics().get(1))
                                                                            .toGeneric()
                                                                            .map(Generic::getExtends)
                                                                            .map(ShadowApi::convert)
                                                                            .flatMap(ShadowConverter::toInterface)
                                                                            .map(Interface::getGenerics)
                                                                            .map(shadows -> shadows.get(0))
                                                                            .orElseThrow(IllegalStateException::new);
                                 assertEquals(shadowApi.getClassOrThrow("java.lang.String"), interpolated);

                                 Interface independentExample = shadowApi.getInterfaceOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                                         .withGenerics(shadowApi.getConstants().getUnboundWildcard());
                                 Interface independentCapture = independentExample.interpolateGenerics();
                                 Shadow<TypeMirror> interpolatedIndependent = convert(independentCapture.getGenerics().get(0))
                                                                                       .toGeneric()
                                                                                       .map(Generic::getExtends)
                                                                                       .orElseThrow(IllegalStateException::new);
                                 assertEquals(shadowApi.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                                 Interface dependentExample = shadowApi.getInterfaceOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                                       .withGenerics(shadowApi.getConstants().getUnboundWildcard(),
                                                                                     shadowApi.getClassOrThrow("java.lang.String"));
                                 Interface dependentCapture = dependentExample.interpolateGenerics();
                                 Shadow<TypeMirror> interpolatedDependent = convert(dependentCapture.getGenerics().get(0))
                                                                                     .toGeneric()
                                                                                     .map(Generic::getExtends)
                                                                                     .orElseThrow(IllegalStateException::new);
                                 assertEquals(shadowApi.getClassOrThrow("java.lang.String"), interpolatedDependent);
                              })
                   .withCodeToCompile("InterpolateGenericsExample.java",
                                        "                           public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {\n" +
                                        "                              interface IndependentGeneric<C> {}\n" +
                                        "                              interface DependentGeneric<D extends E, E> {}\n" +
                                        "                           }")
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
                                 assertEquals(Arrays.asList(shadowApi.getClassOrThrow("java.lang.Object")),
                                              shadowApi.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceNoParent")
                                                       .getDirectSuperTypes());

                                 assertEquals(Arrays.asList(shadowApi.getClassOrThrow("java.lang.Object"),
                                                      shadowApi.getInterfaceOrThrow("java.lang.Comparable"),
                                                      shadowApi.getInterfaceOrThrow("java.util.function.Consumer")),
                                              shadowApi.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceParent")
                                                       .getDirectSuperTypes());
                              })
                   .withCodeToCompile("DirektSuperTypeExample.java", "                           import java.util.function.Consumer;\n" +
                                                                       "                           import java.util.function.Supplier;\n" +
                                                                       "\n" +
                                                                       "                           public class DirektSuperTypeExample {\n" +
                                                                       "                              interface InterfaceNoParent {}\n" +
                                                                       "\n" +
                                                                       "                              interface InterfaceParent extends Comparable<InterfaceParent>,\n" +
                                                                       "                                                                Consumer<InterfaceParent> {}\n" +
                                                                       "                           }")
                   .compile();
   }

   @Test
   @Override
   void testGetSuperTypes()
   {
      ProcessorTest.process(shadowApi ->
                              {
                                 assertEquals(new HashSet<>(Collections.singletonList(shadowApi.getClassOrThrow("java.lang.Object"))),
                                              shadowApi.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceNoParent")
                                                       .getSuperTypes());

                                 assertEquals(new HashSet<>(Arrays.asList(shadowApi.getClassOrThrow("java.lang.Object"),
                                                     shadowApi.getInterfaceOrThrow("java.lang.Comparable"),
                                                     shadowApi.getInterfaceOrThrow("java.util.function.Consumer"))),
                                              shadowApi.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceParent")
                                                       .getSuperTypes());
                              })
                   .withCodeToCompile("DirektSuperTypeExample.java", "                           import java.util.function.Consumer;\n" +
                                                                       "                           import java.util.function.Supplier;\n" +
                                                                       "\n" +
                                                                       "                           public class DirektSuperTypeExample {\n" +
                                                                       "                              interface InterfaceNoParent {}\n" +
                                                                       "\n" +
                                                                       "                              interface InterfaceParent extends Comparable<InterfaceParent>,\n" +
                                                                       "                                                                Consumer<InterfaceParent> {}\n" +
                                                                       "                           }")
                   .compile();
   }
}
