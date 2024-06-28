package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.shadow.type.Annotation;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
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
                               assertTrue(query(shadowApi.getAnnotationOrThrow("java.lang.Override"))
                                                .isSubtypeOf(shadowApi.getInterfaceOrThrow("java.lang.annotation.Annotation")));
                               assertTrue(query(shadowApi.getAnnotationOrThrow("java.lang.Override"))
                                                .isSubtypeOf(shadowApi.getAnnotationOrThrow("java.lang.Override")));
                               assertFalse(query(shadowApi.getAnnotationOrThrow("java.lang.Override"))
                                                 .isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Number")));
                            })
                   .compile();
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      ProcessorTest.process(shadowApi -> assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.Object"),
                                                              shadowApi.getInterfaceOrThrow("java.lang.annotation.Annotation")),
                                                      query(shadowApi.getAnnotationOrThrow("DirektSuperTypeExample.AnnotationNoParent"))
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
      ProcessorTest.process(shadowApi -> assertEquals(Set.of(shadowApi.getClassOrThrow("java.lang.Object"),
                                                             shadowApi.getInterfaceOrThrow("java.lang.annotation.Annotation")),
                                                      query(shadowApi.getAnnotationOrThrow("DirektSuperTypeExample.AnnotationNoParent"))
                                                            .getSuperTypes()))
                   .withCodeToCompile("DirektSuperTypeExample.java", """
                         public class DirektSuperTypeExample {
                            @interface AnnotationNoParent {}
                         }
                         """)
                   .compile();
   }
}
