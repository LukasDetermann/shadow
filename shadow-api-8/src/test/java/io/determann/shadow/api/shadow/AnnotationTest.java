package io.determann.shadow.api.shadow;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationTest<ANNOTATION extends Annotation> extends DeclaredTest<ANNOTATION>
{
   AnnotationTest()
   {
      //noinspection unchecked
      super(shadowApi -> (ANNOTATION) shadowApi.getAnnotationOrThrow("java.lang.annotation.Retention"));
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      ProcessorTest.process(shadowApi ->
                              {
                                 assertTrue(shadowApi.getAnnotationOrThrow("java.lang.Override")
                                                     .isSubtypeOf(shadowApi.getInterfaceOrThrow("java.lang.annotation.Annotation")));
                                 assertTrue(shadowApi.getAnnotationOrThrow("java.lang.Override")
                                                     .isSubtypeOf(shadowApi.getAnnotationOrThrow("java.lang.Override")));
                                 assertFalse(shadowApi.getAnnotationOrThrow("java.lang.Override").isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Number")));
                              })
                   .compile();
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      ProcessorTest.process(shadowApi -> assertEquals(Arrays.asList(shadowApi.getClassOrThrow("java.lang.Object"),
                                                                    shadowApi.getInterfaceOrThrow("java.lang.annotation.Annotation")),
                                                      shadowApi.getAnnotationOrThrow("DirektSuperTypeExample.AnnotationNoParent")
                                                                 .getDirectSuperTypes()))
                   .withCodeToCompile("DirektSuperTypeExample.java", "                           public class DirektSuperTypeExample {\n" +
                                                                       "                              @interface AnnotationNoParent {}\n" +
                                                                       "                           }")
                   .compile();
   }

   @Test
   @Override
   void testGetSuperTypes()
   {
      ProcessorTest.process(shadowApi -> assertEquals(new HashSet<>(Arrays.asList(shadowApi.getClassOrThrow("java.lang.Object"),
                                                                                  shadowApi.getInterfaceOrThrow("java.lang.annotation.Annotation"))),
                                                      shadowApi.getAnnotationOrThrow("DirektSuperTypeExample.AnnotationNoParent")
                                                                 .getSuperTypes()))
                   .withCodeToCompile("DirektSuperTypeExample.java", "                           public class DirektSuperTypeExample {\n" +
                                                                       "                              @interface AnnotationNoParent {}\n" +
                                                                       "                           }")
                   .compile();
   }
}
