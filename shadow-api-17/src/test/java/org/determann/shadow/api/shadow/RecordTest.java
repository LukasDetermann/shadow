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
      super(shadowApi -> shadowApi.getRecord("RecordExample"));
   }

   @Test
   void testGetRecordComponent()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 RecordComponent idComponent = getShadowSupplier().apply(shadowApi).getRecordComponent("id");
                                 assertEquals("id", idComponent.getSimpleName());
                                 assertEquals(shadowApi.getClass("java.lang.Long"), idComponent.getType());

                                 assertThrows(NoSuchElementException.class,
                                              () -> getShadowSupplier().apply(shadowApi).getRecordComponent("asdf"));
                              })
                     .withCodeToCompile("RecordExample.java", "public record RecordExample(Long id) implements java.io.Serializable{}")
                     .compile();
   }

   @Test
   void testGetDirectInterfaces()
   {
      CompilationTest.process(shadowApi -> assertEquals(List.of(shadowApi.getInterface("java.io.Serializable")),
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
                                 assertTrue(getShadowSupplier().apply(shadowApi).isSubtypeOf(shadowApi.getClass("java.lang.Record")));
                                 assertTrue(getShadowSupplier().apply(shadowApi).isSubtypeOf(getShadowSupplier().apply(shadowApi)));
                                 assertFalse(getShadowSupplier().apply(shadowApi).isSubtypeOf(shadowApi.getClass("java.lang.Number")));
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
                                 assertEquals(List.of(shadowApi.getClass("java.lang.Record")),
                                              shadowApi.getRecord("RecordNoParent").getDirectSuperTypes());

                                 assertEquals(List.of(shadowApi.getClass("java.lang.Record"),
                                                      shadowApi.getInterface("java.util.function.Consumer"),
                                                      shadowApi.getInterface("java.util.function.Supplier")),
                                              shadowApi.getRecord("RecordMultiParent").getDirectSuperTypes());
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
                                 assertEquals(Set.of(shadowApi.getClass("java.lang.Object"), shadowApi.getClass("java.lang.Record")),
                                              shadowApi.getRecord("RecordNoParent").getSuperTypes());

                                 assertEquals(Set.of(shadowApi.getClass("java.lang.Object"),
                                                     shadowApi.getClass("java.lang.Record"),
                                                     shadowApi.getInterface("java.util.function.Consumer"),
                                                     shadowApi.getInterface("java.util.function.Supplier")),
                                              shadowApi.getRecord("RecordMultiParent").getSuperTypes());
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
