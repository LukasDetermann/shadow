package org.determann.shadow.api.shadow;

import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationTest<ANNOTATION extends Annotation> extends DeclaredTest<ANNOTATION>
{
   AnnotationTest()
   {
      //noinspection unchecked
      super(shadowApi -> (ANNOTATION) shadowApi.getAnnotation("java.lang.annotation.Retention"));
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertTrue(shadowApi.getAnnotation("java.lang.Override")
                                                     .isSubtypeOf(shadowApi.getInterface("java.lang.annotation.Annotation")));
                                 assertTrue(shadowApi.getAnnotation("java.lang.Override")
                                                     .isSubtypeOf(shadowApi.getAnnotation("java.lang.Override")));
                                 assertFalse(shadowApi.getAnnotation("java.lang.Override").isSubtypeOf(shadowApi.getClass("java.lang.Number")));
                              })
                     .compile();
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      CompilationTest.process(shadowApi -> assertEquals(List.of(shadowApi.getClass("java.lang.Object"),
                                                                shadowApi.getInterface("java.lang.annotation.Annotation")),
                                                        shadowApi.getAnnotation("DirektSuperTypeExample.AnnotationNoParent")
                                                                 .getDirectSuperTypes()))
                     .withCodeToCompile("DirektSuperTypeExample.java", """
                           public class DirektSuperTypeExample {
                              @interface AnnotationNoParent {}
                           }
                           """)
                     .compile();
   }

   @Test
   @Override
   void testGetSuperTypes()
   {
      CompilationTest.process(shadowApi -> assertEquals(Set.of(shadowApi.getClass("java.lang.Object"),
                                                               shadowApi.getInterface("java.lang.annotation.Annotation")),
                                                        shadowApi.getAnnotation("DirektSuperTypeExample.AnnotationNoParent")
                                                                 .getSuperTypes()))
                     .withCodeToCompile("DirektSuperTypeExample.java", """
                           public class DirektSuperTypeExample {
                              @interface AnnotationNoParent {}
                           }
                           """)
                     .compile();
   }
}
