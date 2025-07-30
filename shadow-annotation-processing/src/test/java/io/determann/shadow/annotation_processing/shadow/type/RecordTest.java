package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.type.LM_Generic;
import io.determann.shadow.api.lang_model.shadow.type.LM_Interface;
import io.determann.shadow.api.lang_model.shadow.type.LM_Record;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
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
                               LM_Record declared = context.getRecordOrThrow("InterpolateGenericsExample")
                                                           .withGenerics(context.getClassOrThrow("java.lang.String"),
                                                                         context.getConstants().getUnboundWildcard());

                               LM_Record capture = declared.interpolateGenerics();
                               LM_Type interpolated = Optional.of(((LM_Generic) capture.getGenericTypes().get(1)))
                                                              .map(LM_Generic::getBound)
                                                              .map(LM_Interface.class::cast)
                                                              .map(LM_Interface::getGenericTypes)
                                                              .map(types -> types.get(0))
                                                              .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolated);

                               LM_Record independentExample = context.getRecordOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                                     .withGenerics(context.getConstants().getUnboundWildcard());

                               LM_Record independentCapture = independentExample.interpolateGenerics();
                               LM_Type interpolatedIndependent = Optional.of(((LM_Generic) independentCapture.getGenericTypes().get(0)))
                                                                         .map(LM_Generic::getBound)
                                                                         .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               LM_Record dependentExample = context.getRecordOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                                   .withGenerics(context.getConstants().getUnboundWildcard(),
                                                                                 context.getClassOrThrow("java.lang.String"));

                               LM_Record dependentCapture = dependentExample.interpolateGenerics();
                               LM_Type interpolatedDependent = Optional.of(((LM_Generic) dependentCapture.getGenericTypes().get(0)))
                                                                       .map(LM_Generic::getBound)
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
