package io.determann.shadow.tck.internal.shadow;

import io.determann.shadow.api.shadow.structure.C_Constructor;
import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Interface;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.shadow.modifier.C_Modifier.*;
import static io.determann.shadow.tck.internal.TckTest.withSource;

class ModifiableTest
{
   @Test
   void _public()
   {
      withSource("Test.java", """
            class Test{
               public Test(){};
            }""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Test");
                     C_Constructor constructor = requestOrThrow(cClass, DECLARED_GET_CONSTRUCTORS).get(0);
                     Assertions.assertTrue(requestOrThrow(constructor, MODIFIABLE_HAS_MODIFIER, PUBLIC));
                  });
   }

   @Test
   void _protected()
   {
      withSource("Test.java", """
            class Test{
               protected Test(){};
            }""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Test");
                     C_Constructor constructor = requestOrThrow(cClass, DECLARED_GET_CONSTRUCTORS).get(0);
                     Assertions.assertTrue(requestOrThrow(constructor, MODIFIABLE_HAS_MODIFIER, PROTECTED));
                  });
   }

   @Test
   void _private()
   {
      withSource("Test.java", """
            class Test{
               private Test(){};
            }""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Test");
                     C_Constructor constructor = requestOrThrow(cClass, DECLARED_GET_CONSTRUCTORS).get(0);
                     Assertions.assertTrue(requestOrThrow(constructor, MODIFIABLE_HAS_MODIFIER, PRIVATE));
                  });
   }

   @Test
   void _abstract()
   {
      withSource("Test.java", "abstract class Test{}")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Test");
                     Assertions.assertTrue(requestOrThrow(cClass, MODIFIABLE_HAS_MODIFIER, ABSTRACT));
                  });
   }

   @Test
   void packagePrivate()
   {
      withSource("Test.java", "interface Test{}")
            .test(implementation ->
                  {
                     C_Interface cInterface = requestOrThrow(implementation, GET_INTERFACE, "Test");
                     Assertions.assertTrue(requestOrThrow(cInterface, MODIFIABLE_HAS_MODIFIER, PACKAGE_PRIVATE));
                  });
   }

   @Test
   void _default()
   {
      withSource("Test.java", """
            interface Test{
               default void test(){};
            }""")
            .test(implementation ->
                  {
                     C_Interface cInterface = requestOrThrow(implementation, GET_INTERFACE, "Test");
                     C_Method method = requestOrThrow(cInterface, DECLARED_GET_METHOD, "test").get(0);
                     Assertions.assertTrue(requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, DEFAULT));
                  });
   }

   @Test
   void _static()
   {
      withSource("Test.java", """
            class Test{
               static void test(){};
            }""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Test");
                     C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "test").get(0);
                     Assertions.assertTrue(requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, STATIC));
                  });
   }

   @Test
   void sealed()
   {
      withSource("Parent.java", "sealed interface Parent permits Child {}")
            .withSource("Child.java", "non-sealed interface Child extends Parent {}")
            .test(implementation ->
                  {
                     C_Interface cInterface = requestOrThrow(implementation, GET_INTERFACE, "Parent");
                     Assertions.assertTrue(requestOrThrow(cInterface, MODIFIABLE_HAS_MODIFIER, SEALED));
                  });
   }

   @Test
   void nonSealed()
   {
      withSource("Parent.java", "sealed interface Parent permits Child {}")
            .withSource("Child.java", "non-sealed interface Child extends Parent {}")
            .test(implementation ->
                  {
                     C_Interface cInterface = requestOrThrow(implementation, GET_INTERFACE, "Child");
                     Assertions.assertTrue(requestOrThrow(cInterface, MODIFIABLE_HAS_MODIFIER, NON_SEALED));
                  });
   }

   @Test
   void _final()
   {
      withSource("Test.java", """
            class Test{
               final void test(){};
            }""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Test");
                     C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "test").get(0);
                     Assertions.assertTrue(requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, FINAL));
                  });
   }

   @Test
   void _transient()
   {
      withSource("Test.java", """
            class Test{
               transient int foo;
            }""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Test");
                     C_Field field = requestOrThrow(cClass, DECLARED_GET_FIELD, "foo");
                     Assertions.assertTrue(requestOrThrow(field, MODIFIABLE_HAS_MODIFIER, TRANSIENT));
                  });
   }

   @Test
   void _volatile()
   {
      withSource("Test.java", """
            class Test{
               volatile int foo;
            }""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Test");
                     C_Field field = requestOrThrow(cClass, DECLARED_GET_FIELD, "foo");
                     Assertions.assertTrue(requestOrThrow(field, MODIFIABLE_HAS_MODIFIER, VOLATILE));
                  });
   }

   @Test
   void _synchronized()
   {
      withSource("Test.java", """
            class Test{
               synchronized void test(){};
            }""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Test");
                     C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "test").get(0);
                     Assertions.assertTrue(requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, SYNCHRONIZED));
                  });
   }

   @Test
   void _native()
   {
      withSource("Test.java", """
            class Test{
               native void test();
            }""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Test");
                     C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "test").get(0);
                     Assertions.assertTrue(requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, NATIVE));
                  });
   }

   @Test
   void _strictfp()
   {
      withSource("Test.java", """
            class Test{
               strictfp void test(){};
            }""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Test");
                     C_Method method = requestOrThrow(cClass, DECLARED_GET_METHOD, "test").get(0);
                     Assertions.assertTrue(requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, STRICTFP));
                  });
   }
}
