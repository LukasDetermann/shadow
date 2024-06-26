package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.converter.TypeConverter;
import io.determann.shadow.api.lang_model.LangModelQueries;
import io.determann.shadow.api.lang_model.shadow.type.GenericLangModel;
import io.determann.shadow.api.lang_model.shadow.type.InterfaceLangModel;
import io.determann.shadow.api.shadow.structure.RecordComponent;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.Shadow;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.api.lang_model.LangModelQueries.query;
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
                               RecordComponent idComponent = query(getShadowSupplier().apply(shadowApi)).getRecordComponentOrThrow("id");
                               assertEquals("id", LangModelQueries.query(idComponent).getName());
                               assertEquals(shadowApi.getClassOrThrow("java.lang.Long"), query(idComponent).getType());

                               assertThrows(NoSuchElementException.class,
                                            () -> query(getShadowSupplier().apply(shadowApi)).getRecordComponentOrThrow("asdf"));
                            })
                   .withCodeToCompile("RecordExample.java", "public record RecordExample(Long id) implements java.io.Serializable{}")
                   .compile();
   }

   @Test
   void testGetDirectInterfaces()
   {
      ProcessorTest.process(shadowApi -> assertEquals(List.of(shadowApi.getInterfaceOrThrow("java.io.Serializable")),
                                                      query(getShadowSupplier().apply(shadowApi)).getDirectInterfaces()))
                   .withCodeToCompile("RecordExample.java", "public record RecordExample(Long id) implements java.io.Serializable{}")
                   .compile();
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertTrue(query(getShadowSupplier().apply(shadowApi)).isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Record")));
                               assertTrue(query(getShadowSupplier().apply(shadowApi)).isSubtypeOf(getShadowSupplier().apply(shadowApi)));
                               assertFalse(query(getShadowSupplier().apply(shadowApi)).isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Number")));
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
                                            query(shadowApi.getRecordOrThrow("RecordNoParent")).getDirectSuperTypes());

                               assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.Record"),
                                                    shadowApi.getInterfaceOrThrow("java.util.function.Consumer"),
                                                    shadowApi.getInterfaceOrThrow("java.util.function.Supplier")),
                                            query(shadowApi.getRecordOrThrow("RecordMultiParent")).getDirectSuperTypes());
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
                                            query(shadowApi.getRecordOrThrow("RecordNoParent")).getSuperTypes());

                               assertEquals(Set.of(shadowApi.getClassOrThrow("java.lang.Object"),
                                                   shadowApi.getClassOrThrow("java.lang.Record"),
                                                   shadowApi.getInterfaceOrThrow("java.util.function.Consumer"),
                                                   shadowApi.getInterfaceOrThrow("java.util.function.Supplier")),
                                            query(shadowApi.getRecordOrThrow("RecordMultiParent")).getSuperTypes());
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
                                            () -> shadowApi.withGenerics(shadowApi.getRecordOrThrow("InterpolateGenericsExample.DependentGeneric"),
                                                                         "java.lang.String"));

                               assertThrows(IllegalArgumentException.class,
                                            () -> shadowApi.withGenerics(shadowApi.getRecordOrThrow("SimpleRecord"), "java.io.Serializable"));

                               assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.String")),
                                            query(shadowApi.withGenerics(shadowApi.getRecordOrThrow(
                                                                               "InterpolateGenericsExample.IndependentGeneric"),
                                                                         "java.lang.String"))
                                                  .getGenericTypes());

                               assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.String"),
                                                    shadowApi.getClassOrThrow("java.lang.Number")),
                                            query(shadowApi.withGenerics(shadowApi.getRecordOrThrow("InterpolateGenericsExample.DependentGeneric"),
                                                                   "java.lang.String",
                                                                   "java.lang.Number"))
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
   void testInterpolateGenerics()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Record declared = shadowApi.withGenerics(shadowApi.getRecordOrThrow("InterpolateGenericsExample"),
                                                                                  shadowApi.getClassOrThrow("java.lang.String"),
                                                                                  shadowApi.getConstants().getUnboundWildcard());
                               Record capture = shadowApi.interpolateGenerics(declared);
                               Shadow interpolated = convert(query(capture).getGenericTypes().get(1))
                                     .toGeneric()
                                     .map(LangModelQueries::query)
                                     .map(GenericLangModel::getExtends)
                                     .map(Converter::convert)
                                     .flatMap(TypeConverter::toInterface)
                                     .map(LangModelQueries::query)
                                     .map(InterfaceLangModel::getGenericTypes)
                                     .map(shadows -> shadows.get(0))
                                     .orElseThrow();
                               assertEquals(shadowApi.getClassOrThrow("java.lang.String"), interpolated);

                               Record independentExample = shadowApi.withGenerics(shadowApi.getRecordOrThrow(
                                     "InterpolateGenericsExample.IndependentGeneric"), shadowApi.getConstants().getUnboundWildcard());
                               Record independentCapture = shadowApi.interpolateGenerics(independentExample);
                               Shadow interpolatedIndependent = convert(query(independentCapture).getGenericTypes().get(0))
                                     .toGeneric()
                                     .map(LangModelQueries::query)
                                     .map(GenericLangModel::getExtends)
                                     .orElseThrow();
                               assertEquals(shadowApi.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               Record dependentExample = shadowApi.withGenerics(shadowApi.getRecordOrThrow(
                                                                                      "InterpolateGenericsExample.DependentGeneric"),
                                                                                          shadowApi.getConstants().getUnboundWildcard(),
                                                                                          shadowApi.getClassOrThrow("java.lang.String"));
                               Record dependentCapture = shadowApi.interpolateGenerics(dependentExample);
                               Shadow interpolatedDependent = convert(query(dependentCapture).getGenericTypes().get(0))
                                     .toGeneric()
                                     .map(LangModelQueries::query)
                                     .map(GenericLangModel::getExtends)
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
