package io.determann.shadow.consistency.shadow.type;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.LM_QualifiedNameable;
import io.determann.shadow.api.lang_model.shadow.type.LM_Generic;
import io.determann.shadow.api.lang_model.shadow.type.LM_Interface;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InterfaceTest extends DeclaredTest<LM_Interface>
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
                                                  .map(LM_QualifiedNameable::getQualifiedName)
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
                                            () -> context.getInterfaceOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                         .withGenerics("java.lang.String"));

                               assertThrows(IllegalArgumentException.class,
                                            () -> context.getInterfaceOrThrow("java.io.Serializable").withGenerics("java.io.Serializable"));

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String")),
                                            context.getInterfaceOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                   .withGenerics("java.lang.String")
                                                   .getGenericTypes());

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String"),
                                                    context.getClassOrThrow("java.lang.Number")),
                                            context.getInterfaceOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                   .withGenerics("java.lang.String",
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
                               LM_Interface declared = context.getInterfaceOrThrow("InterpolateGenericsExample")
                                                              .withGenerics(context.getClassOrThrow("java.lang.String"),
                                                                            context.getConstants().getUnboundWildcard());

                               LM_Interface capture = declared.interpolateGenerics();
                               LM_Type interpolated = Optional.of(((LM_Generic) capture.getGenericTypes().get(1)))
                                                              .map(LM_Generic::getExtends)
                                                              .map(LM_Interface.class::cast)
                                                              .map(LM_Interface::getGenericTypes)
                                                              .map(types -> types.get(0))
                                                              .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolated);

                               LM_Interface independentExample = context.getInterfaceOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                                        .withGenerics(context.getConstants().getUnboundWildcard());

                               LM_Interface independentCapture = independentExample.interpolateGenerics();
                            LM_Type interpolatedIndependent = Optional.of(((LM_Generic) independentCapture.getGenericTypes().get(0)))
                                                                      .map(LM_Generic::getExtends)
                                                                      .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               LM_Interface dependentExample = context.getInterfaceOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                                      .withGenerics(context.getConstants().getUnboundWildcard(),
                                                                                    context.getClassOrThrow("java.lang.String"));

                               LM_Interface dependentCapture = dependentExample.interpolateGenerics();
                               LM_Type interpolatedDependent = Optional.of((LM_Generic) dependentCapture.getGenericTypes().get(0))
                                                                       .map(LM_Generic::getExtends)
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
                               assertTrue(getTypeSupplier().apply(context).isSubtypeOf(context.getClassOrThrow("java.lang.Object")));
                               assertTrue(getTypeSupplier().apply(context).isSubtypeOf(getTypeSupplier().apply(context)));
                               assertFalse(getTypeSupplier().apply(context).isSubtypeOf(context.getClassOrThrow("java.lang.Number")));
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
