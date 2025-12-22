package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationTest
{
   @Test
   void isSubtypeOf()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Annotation overwrite = context.getAnnotationOrThrow("java.lang.Override");
                               Ap.Interface annotation = context.getInterfaceOrThrow("java.lang.annotation.Annotation");
                               Ap.Class number = context.getClassOrThrow("java.lang.Number");

                               assertTrue(overwrite.isSubtypeOf(annotation));
                               assertTrue(overwrite.isSubtypeOf(overwrite));
                               assertFalse(overwrite.isSubtypeOf(number));
                            })
                   .compile();
   }

   @Test
   void getDirectSuperTypes()
   {
      ProcessorTest.process(context ->
                            {
                               List<Ap.Declared> expected = List.of(context.getClassOrThrow("java.lang.Object"),
                                                                    context.getInterfaceOrThrow("java.lang.annotation.Annotation"));

                               Ap.Annotation annotation = context.getAnnotationOrThrow("DirektSuperTypeExample.AnnotationNoParent");
                               assertEquals(expected, annotation.getDirectSuperTypes());
                            })
                   .withCodeToCompile("DirektSuperTypeExample.java", """
                                                                     public class DirektSuperTypeExample {
                                                                        @interface AnnotationNoParent {}
                                                                     }
                                                                     """)
                   .compile();
   }

   @Test
   void getSuperTypes()
   {
      ProcessorTest.process(context ->
                            {
                               Set<Ap.Declared> expected = Set.of(context.getClassOrThrow("java.lang.Object"),
                                                                  context.getInterfaceOrThrow("java.lang.annotation.Annotation"));

                               Ap.Annotation annotation = context.getAnnotationOrThrow("DirektSuperTypeExample.AnnotationNoParent");
                               assertEquals(expected, annotation.getSuperTypes());
                            })
                   .withCodeToCompile("DirektSuperTypeExample.java", """
                                                                     public class DirektSuperTypeExample {
                                                                        @interface AnnotationNoParent {}
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
                                                    public @interface Outer {
                                                          @interface Inner {}
                                                      }
                                                    """)
                   .compile();
   }
}
