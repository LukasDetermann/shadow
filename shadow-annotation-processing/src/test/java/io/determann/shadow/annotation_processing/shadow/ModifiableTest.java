package io.determann.shadow.annotation_processing.shadow;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Modifier.*;

class ModifiableTest
{
   @Test
   void _public()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("Test");
                               Ap.Constructor constructor = cClass.getConstructors().get(0);
                               Assertions.assertTrue(constructor.hasModifier(PUBLIC));
                            }).withCodeToCompile("Test.java", """
                                                              class Test{
                                                                 public Test(){};
                                                              }""")
                   .compile();
   }

   @Test
   void _protected()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("Test");
                               Ap.Constructor constructor = cClass.getConstructors().get(0);
                               Assertions.assertTrue(constructor.hasModifier(PROTECTED));
                            }).withCodeToCompile("Test.java", """
                                                              class Test{
                                                                 protected Test(){};
                                                              }""")
                   .compile();
   }

   @Test
   void _private()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("Test");
                               Ap.Constructor constructor = cClass.getConstructors().get(0);
                               Assertions.assertTrue(constructor.hasModifier(PRIVATE));
                            }).withCodeToCompile("Test.java", """
                                                              class Test{
                                                                 private Test(){};
                                                              }""")
                   .compile();
   }

   @Test
   void _abstract()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("Test");
                               Assertions.assertTrue(cClass.hasModifier(ABSTRACT));
                            }).withCodeToCompile("Test.java", "abstract class Test{}")
                   .compile();
   }

   @Test
   void packagePrivate()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Interface cInterface = context.getInterfaceOrThrow("Test");
                               Assertions.assertTrue(cInterface.hasModifier(PACKAGE_PRIVATE));
                            }).withCodeToCompile("Test.java", "interface Test{}")
                   .compile();
   }

   @Test
   void _default()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Interface cInterface = context.getInterfaceOrThrow("Test");
                               Ap.Method method = cInterface.getMethods("test").get(0);
                               Assertions.assertTrue(method.hasModifier(DEFAULT));
                            }).withCodeToCompile("Test.java", """
                                                              interface Test{
                                                                 default void test(){};
                                                              }""")
                   .compile();
   }

   @Test
   void _static()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("Test");
                               Ap.Method method = cClass.getMethods("test").get(0);
                               Assertions.assertTrue(method.hasModifier(STATIC));
                            }).withCodeToCompile("Test.java", """
                                                              class Test{
                                                                 static void test(){};
                                                              }""")
                   .compile();
   }

   @Test
   void sealed()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Interface cInterface = context.getInterfaceOrThrow("Parent");
                               Assertions.assertTrue(cInterface.hasModifier(SEALED));
                            }).withCodeToCompile("Parent.java", "sealed interface Parent permits Child {}")
                   .withCodeToCompile("Child.java", "non-sealed interface Child extends Parent {}")
                   .compile();
   }

   @Test
   void nonSealed()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Interface cInterface = context.getInterfaceOrThrow("Child");
                               Assertions.assertTrue(cInterface.hasModifier(NON_SEALED));
                            }).withCodeToCompile("Parent.java", "sealed interface Parent permits Child {}")
                   .withCodeToCompile("Child.java", "non-sealed interface Child extends Parent {}")
                   .compile();
   }

   @Test
   void _final()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("Test");
                               Ap.Method method = cClass.getMethods("test").get(0);
                               Assertions.assertTrue(method.hasModifier(FINAL));
                            }).withCodeToCompile("Test.java", """
                                                              class Test{
                                                                 final void test(){};
                                                              }""")
                   .compile();
   }

   @Test
   void _transient()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("Test");
                               Ap.Field field = cClass.getFieldOrThrow("foo");
                               Assertions.assertTrue(field.hasModifier(TRANSIENT));
                            }).withCodeToCompile("Test.java", """
                                                              class Test{
                                                                 transient int foo;
                                                              }""")
                   .compile();
   }

   @Test
   void _volatile()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("Test");
                               Ap.Field field = cClass.getFieldOrThrow("foo");
                               Assertions.assertTrue(field.hasModifier(VOLATILE));
                            }).withCodeToCompile("Test.java", """
                                                              class Test{
                                                                 volatile int foo;
                                                              }""")
                   .compile();
   }

   @Test
   void _synchronized()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("Test");
                               Ap.Method method = cClass.getMethods("test").get(0);
                               Assertions.assertTrue(method.hasModifier(SYNCHRONIZED));
                            }).withCodeToCompile("Test.java", """
                                                              class Test{
                                                                 synchronized void test(){};
                                                              }""")
                   .compile();
   }

   @Test
   void _native()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("Test");
                               Ap.Method method = cClass.getMethods("test").get(0);
                               Assertions.assertTrue(method.hasModifier(NATIVE));
                            }).withCodeToCompile("Test.java", """
                                                              class Test{
                                                                 native void test();
                                                              }""")
                   .compile();
   }

   @Test
   void _strictfp()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("Test");
                               Ap.Method method = cClass.getMethods("test").get(0);
                               Assertions.assertTrue(method.hasModifier(STRICTFP));
                            }).withCodeToCompile("Test.java", """
                                                              class Test{
                                                                 strictfp void test(){};
                                                              }""")
                   .compile();
   }
}
