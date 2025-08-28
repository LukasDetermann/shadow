package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InterfaceTest
{
   @Test
   void withGenerics()
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
   void interpolateGenerics()
   {
      ProcessorTest.process(context ->
                            {
                               AP.Interface declared = context.getInterfaceOrThrow("InterpolateGenericsExample")
                                                              .withGenerics(context.getClassOrThrow("java.lang.String"),
                                                                            context.getConstants().getUnboundWildcard());

                               AP.Interface capture = declared.interpolateGenerics();
                               AP.Type interpolated = Optional.of(((AP.Generic) capture.getGenericTypes().get(1)))
                                                              .map(AP.Generic::getBound)
                                                              .map(AP.Interface.class::cast)
                                                              .map(AP.Interface::getGenericTypes)
                                                              .map(types -> types.get(0))
                                                              .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolated);

                               AP.Interface independentExample = context.getInterfaceOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                                        .withGenerics(context.getConstants().getUnboundWildcard());

                               AP.Interface independentCapture = independentExample.interpolateGenerics();
                               AP.Type interpolatedIndependent = Optional.of(((AP.Generic) independentCapture.getGenericTypes().get(0)))
                                                                         .map(AP.Generic::getBound)
                                                                         .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               AP.Interface dependentExample = context.getInterfaceOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                                      .withGenerics(context.getConstants().getUnboundWildcard(),
                                                                                    context.getClassOrThrow("java.lang.String"));

                               AP.Interface dependentCapture = dependentExample.interpolateGenerics();
                               AP.Type interpolatedDependent = Optional.of((AP.Generic) dependentCapture.getGenericTypes().get(0))
                                                                       .map(AP.Generic::getBound)
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
}
