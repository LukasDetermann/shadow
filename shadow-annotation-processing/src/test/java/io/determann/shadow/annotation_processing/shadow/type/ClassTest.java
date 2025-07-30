package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.LM;
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
                               LM.Class declared = context.getClassOrThrow("InterpolateGenericsExample")
                                                          .withGenerics(context.getClassOrThrow("java.lang.String"),
                                                                        context.getConstants().getUnboundWildcard());

                               LM.Class capture = declared.interpolateGenerics();
                               LM.Type interpolated = Optional.of((LM.Generic) capture.getGenericTypes().get(1))
                                                              .map(LM.Generic::getBound)
                                                              .map(LM.Interface.class::cast)
                                                              .map(LM.Interface::getGenericTypes)
                                                              .map(types -> types.get(0))
                                                              .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolated);

                               LM.Class independentExample = context.getClassOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                                    .withGenerics(context.getConstants().getUnboundWildcard());

                               LM.Class independentCapture = independentExample.interpolateGenerics();
                               LM.Type interpolatedIndependent = Optional.of(((LM.Generic) independentCapture.getGenericTypes().get(0)))
                                                                         .map(LM.Generic::getBound)
                                                                         .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               LM.Class dependentExample = context.getClassOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                                  .withGenerics(context.getConstants().getUnboundWildcard(),
                                                                                context.getClassOrThrow("java.lang.String"));

                               LM.Class dependentCapture = dependentExample.interpolateGenerics();
                               LM.Type interpolatedDependent = Optional.of(((LM.Generic) dependentCapture.getGenericTypes().get(0)))
                                                                       .map(LM.Generic::getBound)
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
