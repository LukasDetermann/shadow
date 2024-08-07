package io.determann.shadow.consistency;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.shadow.annotationusage.AnnotationUsage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnnotationableTest
{
   @Test
   void getAnnotationsTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertTrue(query(shadowApi.getClassOrThrow("NotAnnotated")).getAnnotationUsages().isEmpty());

                               List<AnnotationUsage> annotations = query(shadowApi.getClassOrThrow("Child"))
                                                                            .getAnnotationUsages();

                               assertEquals(2, annotations.size());
                               assertEquals("ParentAnnotation", query(query(annotations.get(0)).getAnnotation()).getQualifiedName());
                               assertEquals("ChildAnnotation", query(query(annotations.get(1)).getAnnotation()).getQualifiedName());
                            })
                   .withCodeToCompile("NotAnnotated.java", "class NotAnnotated{}")
                   .withCodeToCompile("ParentAnnotation.java", "@java.lang.annotation.Inherited @interface ParentAnnotation{}")
                   .withCodeToCompile("ChildAnnotation.java", "@interface ChildAnnotation{}")
                   .withCodeToCompile("Parent.java", "@ParentAnnotation class Parent{}")
                   .withCodeToCompile("Child.java", "@ChildAnnotation class Child extends Parent{}")
                   .compile();
   }

   @Test
   void getDirectAnnotationsTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertTrue(query(shadowApi.getClassOrThrow("NotAnnotated")).getDirectAnnotationUsages().isEmpty());

                               List<AnnotationUsage> directAnnotations = query(shadowApi.getClassOrThrow("Child"))
                                                                                  .getDirectAnnotationUsages();

                               assertEquals(1, directAnnotations.size());
                               assertEquals("ChildAnnotation", query(query(directAnnotations.get(0)).getAnnotation()).getQualifiedName());
                            })
                   .withCodeToCompile("NotAnnotated.java", "class NotAnnotated{}")
                   .withCodeToCompile("ParentAnnotation.java", "@java.lang.annotation.Inherited @interface ParentAnnotation{}")
                   .withCodeToCompile("ChildAnnotation.java", "@interface ChildAnnotation{}")
                   .withCodeToCompile("Parent.java", "@ParentAnnotation class Parent{}")
                   .withCodeToCompile("Child.java", "@ChildAnnotation class Child extends Parent{}")
                   .compile();
   }
}
