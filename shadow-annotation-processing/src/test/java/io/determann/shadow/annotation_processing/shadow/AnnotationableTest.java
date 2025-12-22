package io.determann.shadow.annotation_processing.shadow;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationableTest
{
   @Test
   void emptyAnnotations()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Declared cDeclared = context.getDeclaredOrThrow("NotAnnotated");
                               assertTrue(cDeclared.getAnnotationUsages().isEmpty());
                               assertTrue(cDeclared.getDirectAnnotationUsages().isEmpty());
                            })
                   .withCodeToCompile("NotAnnotated.java", "class NotAnnotated{}")
                   .compile();
   }

   @Test
   void inheritance()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Declared cDeclared = context.getDeclaredOrThrow("Child");
                               Ap.Annotation standaloneAnnotation = context.getAnnotationOrThrow("StandaloneAnnotation");
                               Ap.Annotation inheritedAnnotation = context.getAnnotationOrThrow("InheritedAnnotation");

                               //direct
                               assertTrue(cDeclared.getDirectAnnotationUsages().isEmpty());
                               assertFalse(cDeclared.isDirectlyAnnotatedWith(standaloneAnnotation));
                               assertFalse(cDeclared.isDirectlyAnnotatedWith(inheritedAnnotation));

                               assertTrue(cDeclared.getDirectUsageOf(standaloneAnnotation).isEmpty());
                               assertTrue(cDeclared.getDirectUsagesOf(inheritedAnnotation).isEmpty());

                               //inherited
                               List<Ap.AnnotationUsage> usages = cDeclared.getAnnotationUsages();
                               assertEquals(1, usages.size());
                               Ap.Annotation cAnnotation =  usages.get(0).getAnnotation();
                               assertEquals("InheritedAnnotation", cAnnotation.getQualifiedName());
                               assertFalse(cDeclared.isAnnotatedWith(standaloneAnnotation));
                               assertTrue(cDeclared.isAnnotatedWith(inheritedAnnotation));

                               assertTrue(cDeclared.getUsageOf(standaloneAnnotation).isEmpty());
                               assertTrue(cDeclared.getUsageOf(inheritedAnnotation).isPresent());
                            }).withCodeToCompile("InheritedAnnotation.java", "@java.lang.annotation.Inherited @interface InheritedAnnotation{}")
                   .withCodeToCompile("StandaloneAnnotation.java", "@interface StandaloneAnnotation{}")
                   .withCodeToCompile("Parent.java", "@InheritedAnnotation @StandaloneAnnotation class Parent{}")
                   .withCodeToCompile("Child.java", "class Child extends Parent{}")
                   .compile();
   }
}
