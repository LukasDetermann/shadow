package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.shadow.type.Annotation;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationTest<ANNOTATION extends Annotation> extends DeclaredTest<ANNOTATION>
{
   AnnotationTest()
   {
      //noinspection unchecked
      super(context -> (ANNOTATION) context.getAnnotationOrThrow("java.lang.annotation.Retention"));
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      ProcessorTest.process(context ->
                            {
                               assertTrue(context.getAnnotationOrThrow("java.lang.Override")
                                                   .isSubtypeOf(context.getInterfaceOrThrow("java.lang.annotation.Annotation")));

                               assertTrue(context.getAnnotationOrThrow("java.lang.Override")
                                                   .isSubtypeOf(context.getAnnotationOrThrow("java.lang.Override")));
                               
                               assertFalse(context.getAnnotationOrThrow("java.lang.Override")
                                                 .isSubtypeOf(context.getClassOrThrow("java.lang.Number")));
                            })
                   .compile();
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      ProcessorTest.process(context -> assertEquals(List.of(context.getClassOrThrow("java.lang.Object"),
                                                              context.getInterfaceOrThrow("java.lang.annotation.Annotation")),
                                                      context.getAnnotationOrThrow("DirektSuperTypeExample.AnnotationNoParent")
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
      ProcessorTest.process(context -> assertEquals(Set.of(context.getClassOrThrow("java.lang.Object"),
                                                             context.getInterfaceOrThrow("java.lang.annotation.Annotation")),
                                                      context.getAnnotationOrThrow("DirektSuperTypeExample.AnnotationNoParent")
                                                            .getSuperTypes()))
                   .withCodeToCompile("DirektSuperTypeExample.java", """
                         public class DirektSuperTypeExample {
                            @interface AnnotationNoParent {}
                         }
                         """)
                   .compile();
   }
}
