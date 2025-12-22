package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EnumTest
{
   @Test
   void isSubtypeOf()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Enum retentionPolicy = context.getEnumOrThrow("java.lang.annotation.RetentionPolicy");
                               Ap.Class object = context.getClassOrThrow("java.lang.Object");
                               Ap.Class number = context.getClassOrThrow("java.lang.Number");

                               assertTrue(retentionPolicy.isSubtypeOf(object));
                               assertTrue(retentionPolicy.isSubtypeOf(retentionPolicy));
                               assertFalse(retentionPolicy.isSubtypeOf(number));
                            })
                   .compile();
   }

   @Test
   void getDirectSuperTypes()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cEnum = context.getClassOrThrow("java.lang.Enum");
                               Ap.Interface consumer = context.getInterfaceOrThrow("java.util.function.Consumer");
                               Ap.Interface supplier = context.getInterfaceOrThrow("java.util.function.Supplier");

                               Ap.Enum noParent = context.getEnumOrThrow("EnumNoParent");
                               assertEquals(List.of(cEnum), noParent.getDirectSuperTypes());

                               Ap.Enum multiParent = context.getEnumOrThrow("EnumMultiParent");
                               assertEquals(List.of(cEnum, consumer, supplier), multiParent.getDirectSuperTypes());
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
   void getSuperTypes()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class object = context.getClassOrThrow("java.lang.Object");
                               Ap.Interface constable = context.getInterfaceOrThrow("java.lang.constant.Constable");
                               Ap.Interface comparable = context.getInterfaceOrThrow("java.lang.Comparable");
                               Ap.Interface serializable = context.getInterfaceOrThrow("java.io.Serializable");
                               Ap.Class cEnum = context.getClassOrThrow("java.lang.Enum");
                               Ap.Interface consumer = context.getInterfaceOrThrow("java.util.function.Consumer");
                               Ap.Interface supplier = context.getInterfaceOrThrow("java.util.function.Supplier");

                               Ap.Enum noParent = context.getEnumOrThrow("EnumNoParent");
                               assertEquals(Set.of(object, constable, comparable, serializable, cEnum), noParent.getSuperTypes());

                               Ap.Enum multiParent = context.getEnumOrThrow("EnumMultiParent");
                               assertEquals(Set.of(object, constable, comparable, serializable, cEnum, consumer, supplier),
                                            multiParent.getSuperTypes());
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
   void getSurounding()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Declared inner = context.getDeclaredOrThrow("Outer.Inner");
                               Ap.Declared outer = inner.getSurrounding().orElseThrow();
                               assertEquals(context.getDeclaredOrThrow("Outer"), outer);
                            })
                   .withCodeToCompile("Outer.java", """
                                                    public enum Outer {
                                                          UNUSED;
                                                          enum Inner {}
                                                      }
                                                    """)
                   .compile();
   }
}
