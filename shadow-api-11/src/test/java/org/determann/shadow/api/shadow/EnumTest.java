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
      super(shadowApi -> shadowApi.getEnum("java.lang.annotation.RetentionPolicy"));
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertTrue(getShadowSupplier().apply(shadowApi).isSubtypeOf(shadowApi.getClass("java.lang.Object")));
                                 assertTrue(getShadowSupplier().apply(shadowApi).isSubtypeOf(getShadowSupplier().apply(shadowApi)));
                                 assertFalse(getShadowSupplier().apply(shadowApi).isSubtypeOf(shadowApi.getClass("java.lang.Number")));
                              }).compile();
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(List.of(shadowApi.getClass("java.lang.Enum")),
                                              shadowApi.getEnum("EnumNoParent")
                                                       .getDirectSuperTypes());

                                 assertEquals(List.of(shadowApi.getClass("java.lang.Enum"),
                                                      shadowApi.getInterface("java.util.function.Consumer"),
                                                      shadowApi.getInterface("java.util.function.Supplier")),
                                              shadowApi.getEnum("EnumMultiParent")
                                                       .getDirectSuperTypes());
                              })
                     .withCodeToCompile("EnumNoParent.java", "enum EnumNoParent{}")
                     .withCodeToCompile("EnumMultiParent.java",
                                        "                           enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {\n" +
                                        "                                 ;\n" +
                                        "                                 @Override\n" +
                                        "                                 public void accept(EnumMultiParent enumMultiParent) {}\n" +
                                        "\n" +
                                        "                                 @Override\n" +
                                        "                                 public EnumMultiParent get() {return null;}\n" +
                                        "                              }")
                     .compile();
   }

   @Test
   @Override
   void testGetSuperTypes()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(Set.of(shadowApi.getClass("java.lang.Object"),
                                                     shadowApi.getInterface("java.lang.constant.Constable"),
                                                     shadowApi.getInterface("java.lang.Comparable"),
                                                     shadowApi.getInterface("java.io.Serializable"),
                                                     shadowApi.getClass("java.lang.Enum")), shadowApi.getEnum("EnumNoParent").getSuperTypes());

                                 assertEquals(Set.of(shadowApi.getClass("java.lang.Object"),
                                                     shadowApi.getInterface("java.lang.constant.Constable"),
                                                     shadowApi.getInterface("java.lang.Comparable"),
                                                     shadowApi.getInterface("java.io.Serializable"),
                                                     shadowApi.getClass("java.lang.Enum"),
                                                     shadowApi.getInterface("java.util.function.Consumer"),
                                                     shadowApi.getInterface("java.util.function.Supplier")),
                                              shadowApi.getEnum("EnumMultiParent")
                                                       .getSuperTypes());
                              })
                     .withCodeToCompile("EnumNoParent.java", "enum EnumNoParent{}")
                     .withCodeToCompile("",
                                        "                           enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {\n" +
                                        "                                 ;\n" +
                                        "                                 @Override\n" +
                                        "                                 public void accept(EnumMultiParent enumMultiParent) {}\n" +
                                        "\n" +
                                        "                                 @Override\n" +
                                        "                                 public EnumMultiParent get() {return null;}\n" +
                                        "                              }")
                     .compile();
   }
}
