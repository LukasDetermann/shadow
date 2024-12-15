package io.determann.shadow.tck.internal.shadow.type;

import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Interface;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.test;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.*;

class ClassTest
{
   @Test
   void getSuperClass()
   {
      test(implementation ->
           {
              C_Class integer = requestOrThrow(implementation, GET_CLASS, "java.lang.Integer");
              C_Class number = requestOrThrow(implementation, GET_CLASS, "java.lang.Number");
              assertEquals(requestOrThrow(integer, CLASS_GET_SUPER_CLASS), number);
           });
   }

   @Test
   void getSuperClassOfObject()
   {
      test(implementation ->
           {
              C_Class object = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");
              assertNull(requestOrThrow(object, CLASS_GET_SUPER_CLASS));
           });
   }

   @Test
   void getPermittedSubClasses()
   {
      withSource("PermittedSubClassesExample.java", """
            public sealed class PermittedSubClassesExample permits PermittedSubClassesExample.Child
            {
               public static final class Child extends PermittedSubClassesExample {}
            }
            """)
            .test(implementation ->
                  {
                     List<C_Class> expected = List.of(requestOrThrow(implementation, GET_CLASS, "PermittedSubClassesExample.Child"));

                     C_Class permittedSubClassesExample = requestOrThrow(implementation, GET_CLASS, "PermittedSubClassesExample");
                     assertEquals(expected, requestOrThrow(permittedSubClassesExample, CLASS_GET_PERMITTED_SUB_CLASSES));
                  });
   }

   @Test
   void getOuterType()
   {
      withSource("OuterTypeExample.java", """
            public class OuterTypeExample {
               private class InnerClass {}
               private static class StaticInnerClass {}
            }
            """)
            .test(implementation ->
                  {
                     C_Class outerTypeExample = requestOrThrow(implementation, GET_CLASS, "OuterTypeExample");
                     C_Class innerClass = requestOrThrow(implementation, GET_CLASS, "OuterTypeExample.InnerClass");
                     C_Class staticInnerClass = requestOrThrow(implementation, GET_CLASS, "OuterTypeExample.StaticInnerClass");

                     assertEquals(outerTypeExample, requestOrThrow(innerClass, CLASS_GET_OUTER_TYPE));

                     assertTrue(requestOrEmpty(outerTypeExample, CLASS_GET_OUTER_TYPE).isEmpty());

                     assertTrue(requestOrEmpty(staticInnerClass, CLASS_GET_OUTER_TYPE).isEmpty());
                  });
   }

   @Test
   void getDirectInterfaces()
   {
      withSource("Direct.java", "public interface Direct{}")
            .withSource("InDirect.java", "public interface InDirect{}")
            .withSource("DirectInterfacesExample.java", """
                  public class DirectInterfacesExample implements InDirect {
                     public static class Child extends DirectInterfacesExample implements Direct {}
                  }
                  """)
            .test(implementation ->
                  {
                     C_Class child = requestOrThrow(implementation, GET_CLASS, "DirectInterfacesExample.Child");
                     List<? extends C_Interface> directInterfaces = requestOrThrow(child, DECLARED_GET_DIRECT_INTERFACES);

                     assertEquals(1, directInterfaces.size());
                     assertEquals(requestOrThrow(implementation, GET_INTERFACE, "Direct"), directInterfaces.get(0));
                  });
   }

   @Test
   void isSubtypeOf()
   {
      test(implementation ->
           {
              C_Class declaredLong = requestOrThrow(implementation, GET_CLASS, "java.lang.Long");
              C_Class number = requestOrThrow(implementation, GET_CLASS, "java.lang.Number");

              assertTrue(requestOrThrow(declaredLong, DECLARED_IS_SUBTYPE_OF, number));
              assertTrue(requestOrThrow(declaredLong, DECLARED_IS_SUBTYPE_OF, declaredLong));
              assertFalse(requestOrThrow(number, DECLARED_IS_SUBTYPE_OF, declaredLong));
           });
   }

   @Test
   void isAssignableFrom()
   {
      test(implementation ->
           {
              C_Class declaredLong = requestOrThrow(implementation, GET_CLASS, "java.lang.Long");
              C_Class integer = requestOrThrow(implementation, GET_CLASS, "java.lang.Integer");
              C_Class number = requestOrThrow(implementation, GET_CLASS, "java.lang.Number");

              assertTrue(requestOrThrow(declaredLong, CLASS_IS_ASSIGNABLE_FROM, number));
              assertTrue(requestOrThrow(declaredLong, CLASS_IS_ASSIGNABLE_FROM, declaredLong));
              assertFalse(requestOrThrow(number, CLASS_IS_ASSIGNABLE_FROM, declaredLong));

              assertTrue(requestOrThrow(integer, CLASS_IS_ASSIGNABLE_FROM, requestOrThrow(implementation, GET_INT)));
           });
   }

   @Test
   void iGetDirectSuperTypes()
   {
      withSource("ClassParent.java", "abstract class ClassParent extends Number {}")
            .withSource("ClassMixedParent.java",
                        "abstract class ClassMixedParent extends Number implements java.lang.Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}")
            .test(implementation ->
                  {
                     C_Class number = requestOrThrow(implementation, GET_CLASS, "java.lang.Number");
                     C_Class classParent = requestOrThrow(implementation, GET_CLASS, "ClassParent");
                     C_Interface comparable = requestOrThrow(implementation, GET_INTERFACE, "java.lang.Comparable");
                     C_Interface consumer = requestOrThrow(implementation, GET_INTERFACE, "java.util.function.Consumer");
                     C_Class classMixedParent = requestOrThrow(implementation, GET_CLASS, "ClassMixedParent");

                     assertEquals(List.of(number), requestOrThrow(classParent, DECLARED_GET_DIRECT_SUPER_TYPES));

                     assertEquals(List.of(number, comparable, consumer), requestOrThrow(classMixedParent, DECLARED_GET_DIRECT_SUPER_TYPES));
                  });
   }

   @Test
   void getSuperTypes()
   {
      withSource("ClassNoParent.java", "class ClassNoParent {}")
            .withSource("ClassParent.java", "abstract class ClassParent extends Number {}")
            .withSource("ClassMixedParent.java",
                        "abstract class ClassMixedParent extends Number implements java.lang.Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}")
            .test(implementation ->
                  {
                     C_Class object = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");
                     C_Class number = requestOrThrow(implementation, GET_CLASS, "java.lang.Number");
                     C_Interface serializable = requestOrThrow(implementation, GET_INTERFACE, "java.io.Serializable");
                     C_Interface comparable = requestOrThrow(implementation, GET_INTERFACE, "java.lang.Comparable");
                     C_Interface consumer = requestOrThrow(implementation, GET_INTERFACE, "java.util.function.Consumer");

                     assertEquals(Collections.emptySet(), requestOrThrow(object, DECLARED_GET_SUPER_TYPES));

                     C_Class mixedParent = requestOrThrow(implementation, GET_CLASS, "ClassMixedParent");
                     assertEquals(Set.of(object, number, serializable, comparable, consumer),
                                  requestOrThrow(mixedParent, DECLARED_GET_SUPER_TYPES));
                  });
   }
}
