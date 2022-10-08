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
                                 assertTrue(shadowApi.getInterface("java.util.List").getAnnotationUsages().isEmpty());

                                 assertTrue(shadowApi.getClass("jdk.jfr.events.DirectBufferStatisticsEvent")
                                                     .getAnnotationUsages()
                                                     .stream()
                                                     .map(QualifiedNameable::getQualifiedName)
                                                     .anyMatch(name -> name.equals("jdk.jfr.Category")));
                              })
                     .compile();
   }

   @Test
   void getDirectAnnotationsTest()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertTrue(shadowApi.getInterface("java.util.List").getAnnotationUsages().isEmpty());

                                 List<AnnotationUsage> directAnnotations = shadowApi.getAnnotation("java.lang.annotation.Documented").getDirectAnnotationUsages();

                                 assertEquals(3, directAnnotations.size());
                                 assertEquals("java.lang.annotation.Documented", directAnnotations.get(0).getQualifiedName());
                                 assertEquals("java.lang.annotation.Retention", directAnnotations.get(1).getQualifiedName());
                                 assertEquals("java.lang.annotation.Target", directAnnotations.get(2).getQualifiedName());

                                 //not contains inherited annotations
                                 assertTrue(shadowApi.getClass("jdk.jfr.events.DirectBufferStatisticsEvent")
                                                     .getDirectAnnotationUsages()
                                                     .stream()
                                                     .map(QualifiedNameable::getQualifiedName)
                                                     .noneMatch(name -> name.equals("jdk.jfr.Category")));
                              })
                     .compile();
   }
}
