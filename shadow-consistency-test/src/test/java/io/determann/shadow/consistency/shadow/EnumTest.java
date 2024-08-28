package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.type.LM_Enum;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EnumTest extends DeclaredTest<LM_Enum>
{
   EnumTest()
   {
      super(context -> context.getEnumOrThrow("java.lang.annotation.RetentionPolicy"));
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      ProcessorTest.process(context ->
                            {
                               assertTrue(getTypeSupplier().apply(context).isSubtypeOf(context.getClassOrThrow("java.lang.Object")));
                               assertTrue(getTypeSupplier().apply(context).isSubtypeOf(getTypeSupplier().apply(context)));
                               assertFalse(getTypeSupplier().apply(context).isSubtypeOf(context.getClassOrThrow("java.lang.Number")));
                            }).compile();
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      ProcessorTest.process(context ->
                            {
                               assertEquals(List.of(context.getClassOrThrow("java.lang.Enum")),
                                            context.getEnumOrThrow("EnumNoParent")
                                                  .getDirectSuperTypes());

                               assertEquals(List.of(context.getClassOrThrow("java.lang.Enum"),
                                                    context.getInterfaceOrThrow("java.util.function.Consumer"),
                                                    context.getInterfaceOrThrow("java.util.function.Supplier")),
                                            context.getEnumOrThrow("EnumMultiParent")
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
      ProcessorTest.process(context ->
                            {
                               assertEquals(Set.of(context.getClassOrThrow("java.lang.Object"),
                                                   context.getInterfaceOrThrow("java.lang.constant.Constable"),
                                                   context.getInterfaceOrThrow("java.lang.Comparable"),
                                                   context.getInterfaceOrThrow("java.io.Serializable"),
                                                   context.getClassOrThrow("java.lang.Enum")),
                                            context.getEnumOrThrow("EnumNoParent").getSuperTypes());

                               assertEquals(Set.of(context.getClassOrThrow("java.lang.Object"),
                                                   context.getInterfaceOrThrow("java.lang.constant.Constable"),
                                                   context.getInterfaceOrThrow("java.lang.Comparable"),
                                                   context.getInterfaceOrThrow("java.io.Serializable"),
                                                   context.getClassOrThrow("java.lang.Enum"),
                                                   context.getInterfaceOrThrow("java.util.function.Consumer"),
                                                   context.getInterfaceOrThrow("java.util.function.Supplier")),
                                            context.getEnumOrThrow("EnumMultiParent").getSuperTypes());
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
}
