package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.type.LM_Generic;
import io.determann.shadow.api.lang_model.shadow.type.LM_Interface;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
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
                               LM_Interface declared = context.getInterfaceOrThrow("InterpolateGenericsExample")
                                                              .withGenerics(context.getClassOrThrow("java.lang.String"),
                                                                            context.getConstants().getUnboundWildcard());

                               LM_Interface capture = declared.interpolateGenerics();
                               LM_Type interpolated = Optional.of(((LM_Generic) capture.getGenericTypes().get(1)))
                                                              .map(LM_Generic::getBound)
                                                              .map(LM_Interface.class::cast)
                                                              .map(LM_Interface::getGenericTypes)
                                                              .map(types -> types.get(0))
                                                              .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolated);

                               LM_Interface independentExample = context.getInterfaceOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                                        .withGenerics(context.getConstants().getUnboundWildcard());

                               LM_Interface independentCapture = independentExample.interpolateGenerics();
                               LM_Type interpolatedIndependent = Optional.of(((LM_Generic) independentCapture.getGenericTypes().get(0)))
                                                                         .map(LM_Generic::getBound)
                                                                         .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               LM_Interface dependentExample = context.getInterfaceOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                                      .withGenerics(context.getConstants().getUnboundWildcard(),
                                                                                    context.getClassOrThrow("java.lang.String"));

                               LM_Interface dependentCapture = dependentExample.interpolateGenerics();
                               LM_Type interpolatedDependent = Optional.of((LM_Generic) dependentCapture.getGenericTypes().get(0))
                                                                       .map(LM_Generic::getBound)
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
