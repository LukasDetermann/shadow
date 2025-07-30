package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.type.LM_Class;
import io.determann.shadow.api.lang_model.shadow.type.LM_Generic;
import io.determann.shadow.api.lang_model.shadow.type.LM_Interface;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClassTest
{
   @Test
   void withGenerics()
   {
      ProcessorTest.process(context ->
                            {
                               assertThrows(IllegalArgumentException.class,
                                            () -> context.getClassOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                         .withGenerics("java.lang.String"));

                               assertThrows(IllegalArgumentException.class,
                                            () -> context.getClassOrThrow("java.lang.String").withGenerics("java.lang.String"));

                               assertEquals(context.getClassOrThrow("java.lang.String"),
                                            context.getClassOrThrow("WithGenericsExample.Inner").withGenerics("java.lang.String")
                                                   .getGenericTypes()
                                                   .get(0));

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String")),
                                            context.getClassOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                   .withGenerics("java.lang.String")
                                                   .getGenericTypes());

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String"),
                                                    context.getClassOrThrow("java.lang.Number")),
                                            context.getClassOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                   .withGenerics("java.lang.String",
                                                                 "java.lang.Number")
                                                   .getGenericTypes());
                            })
                   .withCodeToCompile("InterpolateGenericsExample.java", """
                         public class InterpolateGenericsExample <A extends java.lang.Comparable<B>, B extends java.lang.Comparable<A>> {
                              static class IndependentGeneric<C> {}
                              static class DependentGeneric<D extends E, E> {}
                           }
                         """)
                   .withCodeToCompile("WithGenericsExample.java", """
                         public class WithGenericsExample {
                            class Inner<T> {}
                         }
                         """)
                   .compile();
   }

   @Test
   void interpolateGenerics()
   {
      ProcessorTest.process(context ->
                            {
                               LM_Class declared = context.getClassOrThrow("InterpolateGenericsExample")
                                                          .withGenerics(context.getClassOrThrow("java.lang.String"),
                                                                        context.getConstants().getUnboundWildcard());

                               LM_Class capture = declared.interpolateGenerics();
                               LM_Type interpolated = Optional.of((LM_Generic) capture.getGenericTypes().get(1))
                                                              .map(LM_Generic::getBound)
                                                              .map(LM_Interface.class::cast)
                                                              .map(LM_Interface::getGenericTypes)
                                                              .map(types -> types.get(0))
                                                              .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolated);

                               LM_Class independentExample = context.getClassOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                                    .withGenerics(context.getConstants().getUnboundWildcard());

                               LM_Class independentCapture = independentExample.interpolateGenerics();
                               LM_Type interpolatedIndependent = Optional.of(((LM_Generic) independentCapture.getGenericTypes().get(0)))
                                                                         .map(LM_Generic::getBound)
                                                                         .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               LM_Class dependentExample = context.getClassOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                                  .withGenerics(context.getConstants().getUnboundWildcard(),
                                                                                context.getClassOrThrow("java.lang.String"));

                               LM_Class dependentCapture = dependentExample.interpolateGenerics();
                               LM_Type interpolatedDependent = Optional.of(((LM_Generic) dependentCapture.getGenericTypes().get(0)))
                                                                       .map(LM_Generic::getBound)
                                                                       .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolatedDependent);
                            })
                   .withCodeToCompile("InterpolateGenericsExample.java", """
                         public class InterpolateGenericsExample <A extends Comparable<B>, B extends Comparable<A>> {
                              static class IndependentGeneric<C> {}
                              static class DependentGeneric<D extends E, E> {}
                           }
                         """)
                   .compile();
   }
}
