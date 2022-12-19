package org.determann.shadow.api;

import org.determann.shadow.api.shadow.AnnotationUsage;
import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnnotationableTest
{
   @Test
   void getAnnotationsTest()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertTrue(shadowApi.getClassOrThrow("NotAnnotated").getAnnotationUsages().isEmpty());

                                 List<AnnotationUsage> annotations = shadowApi.getClassOrThrow("Child")
                                                                              .getAnnotationUsages();

                                 assertEquals(2, annotations.size());
                                 assertEquals("ParentAnnotation", annotations.get(0).getQualifiedName());
                                 assertEquals("ChildAnnotation", annotations.get(1).getQualifiedName());
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
      CompilationTest.process(shadowApi ->
                              {
                                 assertTrue(shadowApi.getClassOrThrow("NotAnnotated").getDirectAnnotationUsages().isEmpty());

                                 List<AnnotationUsage> directAnnotations = shadowApi.getClassOrThrow("Child")
                                                                                    .getDirectAnnotationUsages();

                                 assertEquals(1, directAnnotations.size());
                                 assertEquals("ChildAnnotation", directAnnotations.get(0).getQualifiedName());
                              })
                     .withCodeToCompile("NotAnnotated.java", "class NotAnnotated{}")
                     .withCodeToCompile("ParentAnnotation.java", "@java.lang.annotation.Inherited @interface ParentAnnotation{}")
                     .withCodeToCompile("ChildAnnotation.java", "@interface ChildAnnotation{}")
                     .withCodeToCompile("Parent.java", "@ParentAnnotation class Parent{}")
                     .withCodeToCompile("Child.java", "@ChildAnnotation class Child extends Parent{}")
                     .compile();
   }
}
