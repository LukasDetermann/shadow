package io.determann.shadow.api.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.converter.ShadowConverter;
import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static io.determann.shadow.api.ShadowApi.convert;
import static org.junit.jupiter.api.Assertions.*;

class RecordTest extends DeclaredTest<Record>
{
   RecordTest()
   {
      super(shadowApi -> shadowApi.getRecordOrThrow("RecordExample"));
   }

   @Test
   void testgetRecordComponentOrThrow()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               RecordComponent idComponent = getShadowSupplier().apply(shadowApi).getRecordComponentOrThrow("id");
                               assertEquals("id", idComponent.getSimpleName());
                               assertEquals(shadowApi.getClassOrThrow("java.lang.Long"), idComponent.getType());

                               assertThrows(NoSuchElementException.class,
                                            () -> getShadowSupplier().apply(shadowApi).getRecordComponentOrThrow("asdf"));
                            })
                   .withCodeToCompile("RecordExample.java", "public record RecordExample(Long id) implements java.io.Serializable{}")
                   .compile();
   }

   @Test
   void testGetDirectInterfaces()
   {
      ProcessorTest.process(shadowApi -> assertEquals(List.of(shadowApi.getInterfaceOrThrow("java.io.Serializable")),
                                                      getShadowSupplier().apply(shadowApi).getDirectInterfaces()))
                   .withCodeToCompile("RecordExample.java", "public record RecordExample(Long id) implements java.io.Serializable{}")
                   .compile();
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertTrue(getShadowSupplier().apply(shadowApi).isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Record")));
                               assertTrue(getShadowSupplier().apply(shadowApi).isSubtypeOf(getShadowSupplier().apply(shadowApi)));
                               assertFalse(getShadowSupplier().apply(shadowApi).isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Number")));
                            })
                   .withCodeToCompile("RecordExample.java", "public record RecordExample(Long id) implements java.io.Serializable{}")
                   .compile();
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.Record")),
                                            shadowApi.getRecordOrThrow("RecordNoParent").getDirectSuperTypes());

                               assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.Record"),
                                                    shadowApi.getInterfaceOrThrow("java.util.function.Consumer"),
                                                    shadowApi.getInterfaceOrThrow("java.util.function.Supplier")),
                                            shadowApi.getRecordOrThrow("RecordMultiParent").getDirectSuperTypes());
                            })
                   .withCodeToCompile("RecordNoParent.java", "record RecordNoParent() {}")
                   .withCodeToCompile("RecordMultiParent.java", """
                         record RecordMultiParent() implements java.util.function.Consumer<RecordMultiParent>, java.util.function.Supplier<RecordMultiParent> {
                               @Override
                               public void accept(RecordMultiParent recordMultiParent) {}

                               @Override
                               public RecordMultiParent get() {return null;}
                            }""")
                   .compile();
   }

   @Test
   @Override
   void testGetSuperTypes()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(Set.of(shadowApi.getClassOrThrow("java.lang.Object"), shadowApi.getClassOrThrow("java.lang.Record")),
                                            shadowApi.getRecordOrThrow("RecordNoParent").getSuperTypes());

                               assertEquals(Set.of(shadowApi.getClassOrThrow("java.lang.Object"),
                                                   shadowApi.getClassOrThrow("java.lang.Record"),
                                                   shadowApi.getInterfaceOrThrow("java.util.function.Consumer"),
                                                   shadowApi.getInterfaceOrThrow("java.util.function.Supplier")),
                                            shadowApi.getRecordOrThrow("RecordMultiParent").getSuperTypes());
                            })
                   .withCodeToCompile("RecordNoParent.java", "record RecordNoParent() {}")
                   .withCodeToCompile("RecordMultiParent.java", """
                         record RecordMultiParent() implements java.util.function.Consumer<RecordMultiParent>, java.util.function.Supplier<RecordMultiParent> {
                               @Override
                               public void accept(RecordMultiParent recordMultiParent) {}

                               @Override
                               public RecordMultiParent get() {return null;}
                            }""")
                   .compile();
   }

   @Test
   void testWithGenerics()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertThrows(IllegalArgumentException.class,
                                            () -> shadowApi.getRecordOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                           .withGenerics("java.lang.String"));

                               assertThrows(IllegalArgumentException.class,
                                            () -> shadowApi.getRecordOrThrow("SimpleRecord").withGenerics("java.io.Serializable"));

                               assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.String")),
                                            shadowApi.getRecordOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                     .withGenerics("java.lang.String")
                                                     .getGenerics());

                               assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.String"),
                                                    shadowApi.getClassOrThrow("java.lang.Number")),
                                            shadowApi.getRecordOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                     .withGenerics("java.lang.String", "java.lang.Number")
                                                     .getGenerics());
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
   void testInterpolateGenerics()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Record declared = shadowApi.getRecordOrThrow("InterpolateGenericsExample")
                                                          .withGenerics(shadowApi.getClassOrThrow("java.lang.String"),
                                                                        shadowApi.getConstants().getUnboundWildcard());
                               Record capture = declared.interpolateGenerics();
                               Shadow interpolated = convert(capture.getGenerics().get(1))
                                     .toGeneric()
                                     .map(Generic::getExtends)
                                     .map(ShadowApi::convert)
                                     .flatMap(ShadowConverter::toInterface)
                                     .map(Interface::getGenerics)
                                     .map(shadows -> shadows.get(0))
                                     .orElseThrow();
                               assertEquals(shadowApi.getClassOrThrow("java.lang.String"), interpolated);

                               Record independentExample = shadowApi.getRecordOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                                    .withGenerics(shadowApi.getConstants().getUnboundWildcard());
                               Record independentCapture = independentExample.interpolateGenerics();
                               Shadow interpolatedIndependent = convert(independentCapture.getGenerics().get(0))
                                     .toGeneric()
                                     .map(Generic::getExtends)
                                     .orElseThrow();
                               assertEquals(shadowApi.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               Record dependentExample = shadowApi.getRecordOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                                  .withGenerics(shadowApi.getConstants().getUnboundWildcard(),
                                                                                shadowApi.getClassOrThrow("java.lang.String"));
                               Record dependentCapture = dependentExample.interpolateGenerics();
                               Shadow interpolatedDependent = convert(dependentCapture.getGenerics().get(0))
                                     .toGeneric()
                                     .map(Generic::getExtends)
                                     .orElseThrow();
                               assertEquals(shadowApi.getClassOrThrow("java.lang.String"), interpolatedDependent);
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
