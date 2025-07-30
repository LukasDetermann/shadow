package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.LM;
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
                                                   .getGenericTypes());

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String"),
                                                    context.getClassOrThrow("java.lang.Number")),
                                            context.getRecordOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                   .withGenerics("java.lang.String",
                                                                 "java.lang.Number")
                                                   .getGenericTypes());
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
                               LM.Record declared = context.getRecordOrThrow("InterpolateGenericsExample")
                                                           .withGenerics(context.getClassOrThrow("java.lang.String"),
                                                                         context.getConstants().getUnboundWildcard());

                               LM.Record capture = declared.interpolateGenerics();
                               LM.Type interpolated = Optional.of(((LM.Generic) capture.getGenericTypes().get(1)))
                                                              .map(LM.Generic::getBound)
                                                              .map(LM.Interface.class::cast)
                                                              .map(LM.Interface::getGenericTypes)
                                                              .map(types -> types.get(0))
                                                              .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolated);

                               LM.Record independentExample = context.getRecordOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                                     .withGenerics(context.getConstants().getUnboundWildcard());

                               LM.Record independentCapture = independentExample.interpolateGenerics();
                               LM.Type interpolatedIndependent = Optional.of(((LM.Generic) independentCapture.getGenericTypes().get(0)))
                                                                         .map(LM.Generic::getBound)
                                                                         .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               LM.Record dependentExample = context.getRecordOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                                   .withGenerics(context.getConstants().getUnboundWildcard(),
                                                                                 context.getClassOrThrow("java.lang.String"));

                               LM.Record dependentCapture = dependentExample.interpolateGenerics();
                               LM.Type interpolatedDependent = Optional.of(((LM.Generic) dependentCapture.getGenericTypes().get(0)))
                                                                       .map(LM.Generic::getBound)
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
