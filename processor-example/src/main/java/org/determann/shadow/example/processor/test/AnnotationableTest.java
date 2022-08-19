package org.determann.shadow.example.processor.test;

import org.determann.shadow.api.QualifiedNameable;
import org.determann.shadow.api.shadow.AnnotationUsage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnnotationableTest
{
   @Test
   void getAnnotationsTest()
   {
      assertTrue(SHADOW_API.getInterface("java.util.List").getAnnotations().isEmpty());

      assertTrue(SHADOW_API.getClass("jdk.jfr.events.DirectBufferStatisticsEvent")
                           .getAnnotations()
                           .stream()
                           .map(QualifiedNameable::getQualifiedName)
                           .anyMatch(name -> name.equals("jdk.jfr.Category")));
   }

   @Test
   void getDirectAnnotationsTest()
   {
      assertTrue(SHADOW_API.getInterface("java.util.List").getAnnotations().isEmpty());

      List<AnnotationUsage> directAnnotations = SHADOW_API.getAnnotation("java.lang.annotation.Documented").getDirectAnnotations();

      assertEquals(3, directAnnotations.size());
      assertEquals("java.lang.annotation.Documented", directAnnotations.get(0).getQualifiedName());
      assertEquals("java.lang.annotation.Retention", directAnnotations.get(1).getQualifiedName());
      assertEquals("java.lang.annotation.Target", directAnnotations.get(2).getQualifiedName());

      //not contains inherited annotations
      assertTrue(SHADOW_API.getClass("jdk.jfr.events.DirectBufferStatisticsEvent")
                           .getDirectAnnotations()
                           .stream()
                           .map(QualifiedNameable::getQualifiedName)
                           .noneMatch(name -> name.equals("jdk.jfr.Category")));
   }
}
