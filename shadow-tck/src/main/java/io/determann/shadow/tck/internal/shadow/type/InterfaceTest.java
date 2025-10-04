package io.determann.shadow.tck.internal.shadow.type;

import io.determann.shadow.api.C;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.test;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.*;

class InterfaceTest
{
   @Test
   void getDirectInterfaces()
   {
      test(implementation ->
           {
              C.Interface function = requestOrThrow(implementation, GET_INTERFACE, "java.util.function.Function");
              C.Interface unaryOperator = requestOrThrow(implementation, GET_INTERFACE, "java.util.function.UnaryOperator");

              assertEquals(0, requestOrThrow(function, DECLARED_GET_DIRECT_INTERFACES).size());

              assertEquals(List.of(function), requestOrThrow(unaryOperator, DECLARED_GET_DIRECT_INTERFACES));
           });
   }

   @Test
   void isFunctional()
   {
      test(implementation ->
           {
              Predicate<String> isFunctional = name ->
              {
                 C.Interface cInterface = requestOrThrow(implementation, GET_INTERFACE, name);
                 return requestOrThrow(cInterface, INTERFACE_IS_FUNCTIONAL);
              };

              assertTrue(isFunctional.test("java.util.function.Function"));
              assertTrue(isFunctional.test("java.lang.Comparable"));
              assertFalse(isFunctional.test("java.util.List"));
           });
   }

   @Test
   void isSubtypeOf()
   {
      test(implementation ->
           {
              C.Interface function = requestOrThrow(implementation, GET_INTERFACE, "java.util.function.Function");
              C.Class object = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");
              C.Class number = requestOrThrow(implementation, GET_CLASS, "java.lang.Number");

              assertTrue(requestOrThrow(function, DECLARED_IS_SUBTYPE_OF, object));
              assertTrue(requestOrThrow(function, DECLARED_IS_SUBTYPE_OF, function));
              assertFalse(requestOrThrow(function, DECLARED_IS_SUBTYPE_OF, number));
           });
   }

   @Test
   void getDirectSuperTypes()
   {
      withSource("DirektSuperTypeExample.java", """
            import java.util.function.Consumer;
            import java.util.function.Supplier;
            
            public class DirektSuperTypeExample {
               interface InterfaceNoParent {}
            
               interface InterfaceParent extends Comparable<InterfaceParent>,
                                                 Consumer<InterfaceParent> {}
            }
            """)
            .test(implementation ->
                  {
                     C.Class object = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");
                     C.Interface comparable = requestOrThrow(implementation, GET_INTERFACE, "java.lang.Comparable");
                     C.Interface consumer = requestOrThrow(implementation, GET_INTERFACE, "java.util.function.Consumer");

                     C.Interface noParent = requestOrThrow(implementation, GET_INTERFACE, "DirektSuperTypeExample.InterfaceNoParent");
                     assertEquals(List.of(object), requestOrThrow(noParent, DECLARED_GET_DIRECT_SUPER_TYPES));

                     C.Interface parent = requestOrThrow(implementation, GET_INTERFACE, "DirektSuperTypeExample.InterfaceParent");
                     assertEquals(List.of(object, comparable, consumer), requestOrThrow(parent, DECLARED_GET_DIRECT_SUPER_TYPES));
                  });
   }

   @Test
   void getSuperTypes()
   {
      withSource("DirektSuperTypeExample.java", """
            import java.util.function.Consumer;
            import java.util.function.Supplier;
            
            public class DirektSuperTypeExample {
               interface InterfaceNoParent {}
            
               interface InterfaceParent extends Comparable<InterfaceParent>,
                                                 Consumer<InterfaceParent> {}
            }
            """)
            .test(implementation ->
                  {
                     C.Class object = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");
                     C.Interface comparable = requestOrThrow(implementation, GET_INTERFACE, "java.lang.Comparable");
                     C.Interface consumer = requestOrThrow(implementation, GET_INTERFACE, "java.util.function.Consumer");

                     C.Interface noParent = requestOrThrow(implementation, GET_INTERFACE, "DirektSuperTypeExample.InterfaceNoParent");
                     assertEquals(Set.of(object), requestOrThrow(noParent, DECLARED_GET_SUPER_TYPES));

                     C.Interface parent = requestOrThrow(implementation, GET_INTERFACE, "DirektSuperTypeExample.InterfaceParent");
                     assertEquals(Set.of(object, comparable, consumer), requestOrThrow(parent, DECLARED_GET_SUPER_TYPES));
                  });
   }

   @Test
   void getSurounding()
   {
      withSource("Outer.java", """
                         public interface Outer {
                               interface Inner {}
                           }
                         """)
            .test(implementation -> {

               C.Declared inner = requestOrThrow(implementation, GET_DECLARED, "Outer.Inner");
               C.Declared outer = requestOrThrow(inner, DECLARED_GET_SURROUNDING);
               assertEquals(requestOrThrow(implementation, GET_DECLARED, "Outer"), outer);
            });
   }
}
