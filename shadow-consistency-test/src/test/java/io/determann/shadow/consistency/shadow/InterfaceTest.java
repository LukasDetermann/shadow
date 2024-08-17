package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.QualifiedNameableLamgModel;
import io.determann.shadow.api.lang_model.shadow.type.GenericLangModel;
import io.determann.shadow.api.lang_model.shadow.type.InterfaceLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InterfaceTest extends DeclaredTest<InterfaceLangModel>
{
   InterfaceTest()
   {
      super(context -> context.getInterfaceOrThrow("java.util.function.Function"));
   }

   @Test
   void testGetDirectInterfaces()
   {
      ProcessorTest.process(context ->
                            {
                               assertEquals(0, context.getInterfaceOrThrow("java.util.function.Function").getDirectInterfaces().size());

                               assertEquals(List.of("java.util.function.Function"),
                                            context.getInterfaceOrThrow("java.util.function.UnaryOperator")
                                                  .getDirectInterfaces()
                                                  .stream()
                                                  .map(QualifiedNameableLamgModel::getQualifiedName)
                                                  .toList());
                            })
                   .compile();
   }

   @Test
   void testIsFunctional()
   {
      ProcessorTest.process(context ->
                            {
                               assertTrue(context.getInterfaceOrThrow("java.util.function.Function").isFunctional());
                               assertTrue(context.getInterfaceOrThrow("java.lang.Comparable").isFunctional());
                               assertFalse(context.getInterfaceOrThrow("java.util.List").isFunctional());
                            })
                   .compile();
   }

   @Test
   void testWithGenerics()
   {
      ProcessorTest.process(context ->
                            {
                               assertThrows(IllegalArgumentException.class,
                                            () -> context.withGenerics(context.getInterfaceOrThrow(
                                                  "InterpolateGenericsExample.DependentGeneric"), "java.lang.String"));

                               assertThrows(IllegalArgumentException.class,
                                            () -> context.withGenerics(context.getInterfaceOrThrow("java.io.Serializable"),
                                                                         "java.io.Serializable"));

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String")),
                                            context.withGenerics(context.getInterfaceOrThrow("InterpolateGenericsExample.IndependentGeneric"),
                                                                   "java.lang.String")
                                                     .getGenericTypes());

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String"),
                                                    context.getClassOrThrow("java.lang.Number")),
                                            context.withGenerics(context.getInterfaceOrThrow("InterpolateGenericsExample.DependentGeneric"),
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
      ProcessorTest.process(context ->
                            {
                               InterfaceLangModel declared = context.withGenerics(context.getInterfaceOrThrow("InterpolateGenericsExample"),
                                                                           context.getClassOrThrow("java.lang.String"),
                                                                           context.getConstants().getUnboundWildcard());

                               InterfaceLangModel capture = context.interpolateGenerics(declared);
                               ShadowLangModel interpolated = Optional.of(((GenericLangModel) capture.getGenericTypes().get(1)))
                                                                      .map(GenericLangModel::getExtends)
                                                                      .map(InterfaceLangModel.class::cast)
                                                                      .map(InterfaceLangModel::getGenericTypes)
                                                                      .map(shadows -> shadows.get(0))
                                                                      .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolated);

                               InterfaceLangModel independentExample = context.withGenerics(context.getInterfaceOrThrow(
                                                                                           "InterpolateGenericsExample.IndependentGeneric"),
                                                                                     context.getConstants().getUnboundWildcard());
                               InterfaceLangModel independentCapture = context.interpolateGenerics(independentExample);
                            ShadowLangModel interpolatedIndependent = Optional.of(((GenericLangModel) independentCapture.getGenericTypes().get(0)))
                                     .map(GenericLangModel::getExtends)
                                     .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               InterfaceLangModel dependentExample = context.withGenerics(context.getInterfaceOrThrow(
                                                                                         "InterpolateGenericsExample.DependentGeneric"), context.getConstants().getUnboundWildcard(),
                                                                                   context.getClassOrThrow("java.lang.String"));
                               InterfaceLangModel dependentCapture = context.interpolateGenerics(dependentExample);
                               ShadowLangModel interpolatedDependent = Optional.of((GenericLangModel) dependentCapture.getGenericTypes().get(0))
                                     .map(GenericLangModel::getExtends)
                                     .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolatedDependent);
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
      ProcessorTest.process(context ->
                            {
                               assertTrue(getShadowSupplier().apply(context).isSubtypeOf(context.getClassOrThrow("java.lang.Object")));
                               assertTrue(getShadowSupplier().apply(context).isSubtypeOf(getShadowSupplier().apply(context)));
                               assertFalse(getShadowSupplier().apply(context).isSubtypeOf(context.getClassOrThrow("java.lang.Number")));
                            })
                   .compile();
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      ProcessorTest.process(context ->
                            {
                               assertEquals(List.of(context.getClassOrThrow("java.lang.Object")),
                                            context.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceNoParent")
                                                  .getDirectSuperTypes());

                               assertEquals(List.of(context.getClassOrThrow("java.lang.Object"),
                                                    context.getInterfaceOrThrow("java.lang.Comparable"),
                                                    context.getInterfaceOrThrow("java.util.function.Consumer")),
                                            context.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceParent")
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
      ProcessorTest.process(context ->
                            {
                               assertEquals(Set.of(context.getClassOrThrow("java.lang.Object")),
                                            context.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceNoParent")
                                                  .getSuperTypes());

                               assertEquals(Set.of(context.getClassOrThrow("java.lang.Object"),
                                                   context.getInterfaceOrThrow("java.lang.Comparable"),
                                                   context.getInterfaceOrThrow("java.util.function.Consumer")),
                                            context.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceParent")
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
