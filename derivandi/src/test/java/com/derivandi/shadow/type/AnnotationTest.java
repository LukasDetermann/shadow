package com.derivandi.shadow.type;

import com.derivandi.api.Ap;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static com.derivandi.api.test.ProcessorTest.processorTest;
import static org.junit.jupiter.api.Assertions.*;

class AnnotationTest
{
   @Test
   void isSubtypeOf()
   {
      processorTest().process(context ->
                              {
                                 Ap.Annotation overwrite = context.getAnnotationOrThrow("java.lang.Override");
                                 Ap.Interface annotation = context.getInterfaceOrThrow("java.lang.annotation.Annotation");
                                 Ap.Class number = context.getClassOrThrow("java.lang.Number");

                                 assertTrue(overwrite.isSubtypeOf(annotation));
                                 assertTrue(overwrite.isSubtypeOf(overwrite));
                                 assertFalse(overwrite.isSubtypeOf(number));
                              });
   }

   @Test
   void getDirectSuperTypes()
   {
      processorTest().withCodeToCompile("DirektSuperTypeExample.java", """
                                                                       public class DirektSuperTypeExample {
                                                                          @interface AnnotationNoParent {}
                                                                       }
                                                                       """)
                     .process(context ->
                              {
                                 List<Ap.Declared> expected = List.of(context.getClassOrThrow("java.lang.Object"),
                                                                      context.getInterfaceOrThrow("java.lang.annotation.Annotation"));

                                 Ap.Annotation annotation = context.getAnnotationOrThrow("DirektSuperTypeExample.AnnotationNoParent");
                                 assertEquals(expected, annotation.getDirectSuperTypes());
                              });
   }

   @Test
   void getSuperTypes()
   {
      processorTest().withCodeToCompile("DirektSuperTypeExample.java", """
                                                                       public class DirektSuperTypeExample {
                                                                          @interface AnnotationNoParent {}
                                                                       }
                                                                       """)
                     .process(context ->
                              {
                                 Set<Ap.Declared> expected = Set.of(context.getClassOrThrow("java.lang.Object"),
                                                                    context.getInterfaceOrThrow("java.lang.annotation.Annotation"));

                                 Ap.Annotation annotation = context.getAnnotationOrThrow("DirektSuperTypeExample.AnnotationNoParent");
                                 assertEquals(expected, annotation.getSuperTypes());
                              });
   }

   @Test
   void getSurounding()
   {
      processorTest().withCodeToCompile("Outer.java", """
                                                      public @interface Outer {
                                                            @interface Inner {}
                                                        }
                                                      """)
                     .process(context ->
                              {
                                 Ap.Declared inner = context.getDeclaredOrThrow("Outer.Inner");
                                 Ap.Declared outer = inner.getSurrounding().orElseThrow();
                                 assertEquals(context.getDeclaredOrThrow("Outer"), outer);
                              });
   }
}
