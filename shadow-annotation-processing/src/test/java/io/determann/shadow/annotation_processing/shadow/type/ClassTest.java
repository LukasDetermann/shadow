package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
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
                                                   .getGenericUsages()
                                                   .get(0));

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String")),
                                            context.getClassOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                   .withGenerics("java.lang.String")
                                                   .getGenericUsages());

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String"),
                                                    context.getClassOrThrow("java.lang.Number")),
                                            context.getClassOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                   .withGenerics("java.lang.String",
                                                                 "java.lang.Number")
                                                   .getGenericUsages());
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
                               Ap.Class declared = context.getClassOrThrow("InterpolateGenericsExample")
                                                          .withGenerics(context.getClassOrThrow("java.lang.String"),
                                                                        context.getConstants().getUnboundWildcard());

                               Ap.Class capture = declared.interpolateGenerics();
                               Ap.Type interpolated = Optional.of((Ap.Generic) capture.getGenericUsages().get(1))
                                                              .map(Ap.Generic::getBound)
                                                              .map(Ap.Interface.class::cast)
                                                              .map(Ap.Interface::getGenericUsages)
                                                              .map(types -> types.get(0))
                                                              .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolated);

                               Ap.Class independentExample = context.getClassOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                                    .withGenerics(context.getConstants().getUnboundWildcard());

                               Ap.Class independentCapture = independentExample.interpolateGenerics();
                               Ap.Type interpolatedIndependent = Optional.of(((Ap.Generic) independentCapture.getGenericUsages().get(0)))
                                                                         .map(Ap.Generic::getBound)
                                                                         .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               Ap.Class dependentExample = context.getClassOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                                  .withGenerics(context.getConstants().getUnboundWildcard(),
                                                                                context.getClassOrThrow("java.lang.String"));

                               Ap.Class dependentCapture = dependentExample.interpolateGenerics();
                               Ap.Type interpolatedDependent = Optional.of(((Ap.Generic) dependentCapture.getGenericUsages().get(0)))
                                                                       .map(Ap.Generic::getBound)
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
