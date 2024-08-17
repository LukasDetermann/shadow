package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.structure.RecordComponentLangModel;
import io.determann.shadow.api.lang_model.shadow.type.GenericLangModel;
import io.determann.shadow.api.lang_model.shadow.type.InterfaceLangModel;
import io.determann.shadow.api.lang_model.shadow.type.RecordLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecordTest extends DeclaredTest<RecordLangModel>
{
   RecordTest()
   {
      super(context -> context.getRecordOrThrow("RecordExample"));
   }

   @Test
   void testgetRecordComponentOrThrow()
   {
      ProcessorTest.process(context ->
                            {
                               RecordComponentLangModel idComponent = getShadowSupplier().apply(context).getRecordComponentOrThrow("id");
                               assertEquals("id", idComponent.getName());
                               assertEquals(context.getClassOrThrow("java.lang.Long"), idComponent.getType());

                               assertThrows(NoSuchElementException.class,
                                            () -> getShadowSupplier().apply(context).getRecordComponentOrThrow("asdf"));
                            })
                   .withCodeToCompile("RecordExample.java", "public record RecordExample(Long id) implements java.io.Serializable{}")
                   .compile();
   }

   @Test
   void testGetDirectInterfaces()
   {
      ProcessorTest.process(context -> assertEquals(List.of(context.getInterfaceOrThrow("java.io.Serializable")),
                                                      getShadowSupplier().apply(context).getDirectInterfaces()))
                   .withCodeToCompile("RecordExample.java", "public record RecordExample(Long id) implements java.io.Serializable{}")
                   .compile();
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      ProcessorTest.process(context ->
                            {
                               assertTrue(getShadowSupplier().apply(context).isSubtypeOf(context.getClassOrThrow("java.lang.Record")));
                               assertTrue(getShadowSupplier().apply(context).isSubtypeOf(getShadowSupplier().apply(context)));
                               assertFalse(getShadowSupplier().apply(context).isSubtypeOf(context.getClassOrThrow("java.lang.Number")));
                            })
                   .withCodeToCompile("RecordExample.java", "public record RecordExample(Long id) implements java.io.Serializable{}")
                   .compile();
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      ProcessorTest.process(context ->
                            {
                               assertEquals(List.of(context.getClassOrThrow("java.lang.Record")),
                                            context.getRecordOrThrow("RecordNoParent").getDirectSuperTypes());

                               assertEquals(List.of(context.getClassOrThrow("java.lang.Record"),
                                                    context.getInterfaceOrThrow("java.util.function.Consumer"),
                                                    context.getInterfaceOrThrow("java.util.function.Supplier")),
                                            context.getRecordOrThrow("RecordMultiParent").getDirectSuperTypes());
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
      ProcessorTest.process(context ->
                            {
                               assertEquals(Set.of(context.getClassOrThrow("java.lang.Object"), context.getClassOrThrow("java.lang.Record")),
                                            context.getRecordOrThrow("RecordNoParent").getSuperTypes());

                               assertEquals(Set.of(context.getClassOrThrow("java.lang.Object"),
                                                   context.getClassOrThrow("java.lang.Record"),
                                                   context.getInterfaceOrThrow("java.util.function.Consumer"),
                                                   context.getInterfaceOrThrow("java.util.function.Supplier")),
                                            context.getRecordOrThrow("RecordMultiParent").getSuperTypes());
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
      ProcessorTest.process(context ->
                            {
                               assertThrows(IllegalArgumentException.class,
                                            () -> context.withGenerics(context.getRecordOrThrow("InterpolateGenericsExample.DependentGeneric"),
                                                                         "java.lang.String"));

                               assertThrows(IllegalArgumentException.class,
                                            () -> context.withGenerics(context.getRecordOrThrow("SimpleRecord"), "java.io.Serializable"));

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String")),
                                            context.withGenerics(context.getRecordOrThrow(
                                                                               "InterpolateGenericsExample.IndependentGeneric"),
                                                                         "java.lang.String")
                                                  .getGenericTypes());

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String"),
                                                    context.getClassOrThrow("java.lang.Number")),
                                            context.withGenerics(context.getRecordOrThrow("InterpolateGenericsExample.DependentGeneric"),
                                                                   "java.lang.String",
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
   void testInterpolateGenerics()
   {
      ProcessorTest.process(context ->
                            {
                               RecordLangModel declared = context.withGenerics(context.getRecordOrThrow("InterpolateGenericsExample"),
                                                                               context.getClassOrThrow("java.lang.String"),
                                                                               context.getConstants().getUnboundWildcard());

                               RecordLangModel capture = context.interpolateGenerics(declared);
                               ShadowLangModel interpolated = Optional.of(((GenericLangModel) capture.getGenericTypes().get(1)))
                                                                      .map(GenericLangModel::getExtends)
                                                                      .map(InterfaceLangModel.class::cast)
                                                                      .map(InterfaceLangModel::getGenericTypes)
                                                                      .map(shadows -> shadows.get(0))
                                                                      .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolated);

                               RecordLangModel independentExample = context.withGenerics(context.getRecordOrThrow(
                                     "InterpolateGenericsExample.IndependentGeneric"), context.getConstants().getUnboundWildcard());
                               RecordLangModel independentCapture = context.interpolateGenerics(independentExample);
                               ShadowLangModel interpolatedIndependent = Optional.of(((GenericLangModel) independentCapture.getGenericTypes().get(0)))
                                     .map(GenericLangModel::getExtends)
                                     .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               RecordLangModel dependentExample = context.withGenerics(context.getRecordOrThrow(
                                                                                      "InterpolateGenericsExample.DependentGeneric"),
                                                                                          context.getConstants().getUnboundWildcard(),
                                                                                          context.getClassOrThrow("java.lang.String"));
                               RecordLangModel dependentCapture = context.interpolateGenerics(dependentExample);
                               ShadowLangModel interpolatedDependent = Optional.of(((GenericLangModel) dependentCapture.getGenericTypes().get(0)))
                                     .map(GenericLangModel::getExtends)
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
