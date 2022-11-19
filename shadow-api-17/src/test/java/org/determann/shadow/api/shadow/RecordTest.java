package org.determann.shadow.api.shadow;

import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

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
      CompilationTest.process(shadowApi ->
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
      CompilationTest.process(shadowApi -> assertEquals(List.of(shadowApi.getInterfaceOrThrow("java.io.Serializable")),
                                                        getShadowSupplier().apply(shadowApi).getDirectInterfaces()))
                     .withCodeToCompile("RecordExample.java", "public record RecordExample(Long id) implements java.io.Serializable{}")
                     .compile();
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      CompilationTest.process(shadowApi ->
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
      CompilationTest.process(shadowApi ->
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
      CompilationTest.process(shadowApi ->
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
}
