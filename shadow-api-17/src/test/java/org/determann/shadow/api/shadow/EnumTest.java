package org.determann.shadow.api.shadow;

import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EnumTest extends DeclaredTest<Enum>
{
   EnumTest()
   {
      super(shadowApi -> shadowApi.getEnumOrThrow("java.lang.annotation.RetentionPolicy"));
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertTrue(getShadowSupplier().apply(shadowApi).isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Object")));
                                 assertTrue(getShadowSupplier().apply(shadowApi).isSubtypeOf(getShadowSupplier().apply(shadowApi)));
                                 assertFalse(getShadowSupplier().apply(shadowApi).isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Number")));
                              }).compile();
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.Enum")),
                                              shadowApi.getEnumOrThrow("EnumNoParent")
                                                       .getDirectSuperTypes());

                                 assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.Enum"),
                                                      shadowApi.getInterfaceOrThrow("java.util.function.Consumer"),
                                                      shadowApi.getInterfaceOrThrow("java.util.function.Supplier")),
                                              shadowApi.getEnumOrThrow("EnumMultiParent")
                                                       .getDirectSuperTypes());
                              })
                     .withCodeToCompile("EnumNoParent.java", "enum EnumNoParent{}")
                     .withCodeToCompile("EnumMultiParent.java", """
                           enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {
                                 ;
                                 @Override
                                 public void accept(EnumMultiParent enumMultiParent) {}

                                 @Override
                                 public EnumMultiParent get() {return null;}
                              }
                           """)
                     .compile();
   }

   @Test
   @Override
   void testGetSuperTypes()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(Set.of(shadowApi.getClassOrThrow("java.lang.Object"),
                                                     shadowApi.getInterfaceOrThrow("java.lang.constant.Constable"),
                                                     shadowApi.getInterfaceOrThrow("java.lang.Comparable"),
                                                     shadowApi.getInterfaceOrThrow("java.io.Serializable"),
                                                     shadowApi.getClassOrThrow("java.lang.Enum")), shadowApi.getEnumOrThrow("EnumNoParent").getSuperTypes());

                                 assertEquals(Set.of(shadowApi.getClassOrThrow("java.lang.Object"),
                                                     shadowApi.getInterfaceOrThrow("java.lang.constant.Constable"),
                                                     shadowApi.getInterfaceOrThrow("java.lang.Comparable"),
                                                     shadowApi.getInterfaceOrThrow("java.io.Serializable"),
                                                     shadowApi.getClassOrThrow("java.lang.Enum"),
                                                     shadowApi.getInterfaceOrThrow("java.util.function.Consumer"),
                                                     shadowApi.getInterfaceOrThrow("java.util.function.Supplier")),
                                              shadowApi.getEnumOrThrow("EnumMultiParent")
                                                       .getSuperTypes());
                              })
                     .withCodeToCompile("EnumNoParent.java", "enum EnumNoParent{}").withCodeToCompile("", """
                           enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {
                                 ;
                                 @Override
                                 public void accept(EnumMultiParent enumMultiParent) {}

                                 @Override
                                 public EnumMultiParent get() {return null;}
                              }
                           """)
                     .compile();
   }
}
