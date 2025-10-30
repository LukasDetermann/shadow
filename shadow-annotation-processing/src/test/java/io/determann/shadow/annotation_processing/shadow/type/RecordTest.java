package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecordTest
{
   @Test
   void withGenerics()
   {
      ProcessorTest.process(context ->
                            {
                               assertThrows(IllegalArgumentException.class,
                                            () -> context.getRecordOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                         .withGenerics("java.lang.String"));

                               assertThrows(IllegalArgumentException.class,
                                            () -> context.getRecordOrThrow("SimpleRecord")
                                                         .withGenerics("java.io.Serializable"));

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String")),
                                            context.getRecordOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                   .withGenerics("java.lang.String")
                                                   .getGenericUsages());

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String"),
                                                    context.getClassOrThrow("java.lang.Number")),
                                            context.getRecordOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                   .withGenerics("java.lang.String",
                                                                 "java.lang.Number")
                                                   .getGenericUsages());
                            })
                   .withCodeToCompile("InterpolateGenericsExample.java", """
                         public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> () {
                            record IndependentGeneric<C> () {}
                            record DependentGeneric<D extends E, E> () {}
                         }
                         """)
                   .withCodeToCompile("SimpleRecord.java", """
                         public record SimpleRecord () {}
                         """)
                   .compile();
   }

   @Test
   void interpolateGenerics()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Record declared = context.getRecordOrThrow("InterpolateGenericsExample")
                                                           .withGenerics(context.getClassOrThrow("java.lang.String"),
                                                                         context.getConstants().getUnboundWildcard());

                               Ap.Record capture = declared.interpolateGenerics();
                               Ap.Type interpolated = Optional.of(((Ap.Generic) capture.getGenericUsages().get(1)))
                                                              .map(Ap.Generic::getBound)
                                                              .map(Ap.Interface.class::cast)
                                                              .map(Ap.Interface::getGenericUsages)
                                                              .map(types -> types.get(0))
                                                              .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolated);

                               Ap.Record independentExample = context.getRecordOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                                     .withGenerics(context.getConstants().getUnboundWildcard());

                               Ap.Record independentCapture = independentExample.interpolateGenerics();
                               Ap.Type interpolatedIndependent = Optional.of(((Ap.Generic) independentCapture.getGenericUsages().get(0)))
                                                                         .map(Ap.Generic::getBound)
                                                                         .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               Ap.Record dependentExample = context.getRecordOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                                   .withGenerics(context.getConstants().getUnboundWildcard(),
                                                                                 context.getClassOrThrow("java.lang.String"));

                               Ap.Record dependentCapture = dependentExample.interpolateGenerics();
                               Ap.Type interpolatedDependent = Optional.of(((Ap.Generic) dependentCapture.getGenericUsages().get(0)))
                                                                       .map(Ap.Generic::getBound)
                                                                       .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolatedDependent);
                            })
                   .withCodeToCompile("InterpolateGenericsExample.java", """
                         public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> () {
                            record IndependentGeneric<C> () {}
                            record DependentGeneric<D extends E, E> () {}
                         }
                         """)
                   .compile();
   }
}
