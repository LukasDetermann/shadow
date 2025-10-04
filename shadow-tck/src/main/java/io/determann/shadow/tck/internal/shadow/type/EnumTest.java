package io.determann.shadow.tck.internal.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.tck.internal.TckTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.*;

class EnumTest
{
   @Test
   void isSubtypeOf()
   {
      TckTest.test(implementation ->
                   {
                      C.Enum retentionPolicy = requestOrThrow(implementation, GET_ENUM, "java.lang.annotation.RetentionPolicy");
                      C.Class object = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");
                      C.Class number = requestOrThrow(implementation, GET_CLASS, "java.lang.Number");

                      assertTrue(requestOrThrow(retentionPolicy, DECLARED_IS_SUBTYPE_OF, object));
                      assertTrue(requestOrThrow(retentionPolicy, DECLARED_IS_SUBTYPE_OF, retentionPolicy));
                      assertFalse(requestOrThrow(retentionPolicy, DECLARED_IS_SUBTYPE_OF, number));
                   });
   }

   @Test
   void getDirectSuperTypes()
   {
      withSource("EnumNoParent.java", "enum EnumNoParent{}")
            .withSource("EnumMultiParent.java", """
                  enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {
                        ;
                        @Override
                        public void accept(EnumMultiParent enumMultiParent) {}
                  
                        @Override
                        public EnumMultiParent get() {return null;}
                     }
                  """)
            .test(implementation ->
                  {
                     C.Class cEnum = requestOrThrow(implementation, GET_CLASS, "java.lang.Enum");
                     C.Interface consumer = requestOrThrow(implementation, GET_INTERFACE, "java.util.function.Consumer");
                     C.Interface supplier = requestOrThrow(implementation, GET_INTERFACE, "java.util.function.Supplier");

                     C.Enum noParent = requestOrThrow(implementation, GET_ENUM, "EnumNoParent");
                     assertEquals(List.of(cEnum), requestOrThrow(noParent, DECLARED_GET_DIRECT_SUPER_TYPES));

                     C.Enum multiParent = requestOrThrow(implementation, GET_ENUM, "EnumMultiParent");
                     assertEquals(List.of(cEnum, consumer, supplier), requestOrThrow(multiParent, DECLARED_GET_DIRECT_SUPER_TYPES));
                  });
   }

   @Test
   void getSuperTypes()
   {
      withSource("EnumNoParent.java", "enum EnumNoParent{}")
            .withSource("EnumMultiParent.java", """
                  enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {
                        ;
                        @Override
                        public void accept(EnumMultiParent enumMultiParent) {}
                  
                        @Override
                        public EnumMultiParent get() {return null;}
                     }
                  """)
            .test(implementation ->
                  {
                     C.Class object = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");
                     C.Interface constable = requestOrThrow(implementation, GET_INTERFACE, "java.lang.constant.Constable");
                     C.Interface comparable = requestOrThrow(implementation, GET_INTERFACE, "java.lang.Comparable");
                     C.Interface serializable = requestOrThrow(implementation, GET_INTERFACE, "java.io.Serializable");
                     C.Class cEnum = requestOrThrow(implementation, GET_CLASS, "java.lang.Enum");
                     C.Interface consumer = requestOrThrow(implementation, GET_INTERFACE, "java.util.function.Consumer");
                     C.Interface supplier = requestOrThrow(implementation, GET_INTERFACE, "java.util.function.Supplier");

                     C.Enum noParent = requestOrThrow(implementation, GET_ENUM, "EnumNoParent");
                     assertEquals(Set.of(object, constable, comparable, serializable, cEnum),
                                  requestOrThrow(noParent, DECLARED_GET_SUPER_TYPES));

                     C.Enum multiParent = requestOrThrow(implementation, GET_ENUM, "EnumMultiParent");
                     assertEquals(Set.of(object, constable, comparable, serializable, cEnum, consumer, supplier),
                                  requestOrThrow(multiParent, DECLARED_GET_SUPER_TYPES));
                  });
   }

   @Test
   void getSurounding()
   {
      withSource("Outer.java", """
                         public enum Outer {
                               UNUSED;
                               enum Inner {}
                           }
                         """)
            .test(implementation -> {

               C.Declared inner = requestOrThrow(implementation, GET_DECLARED, "Outer.Inner");
               C.Declared outer = requestOrThrow(inner, DECLARED_GET_SURROUNDING);
               assertEquals(requestOrThrow(implementation, GET_DECLARED, "Outer"), outer);
            });
   }
}
