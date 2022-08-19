package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Annotation;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.*;

public class AnnotationTest<ANNOTATION extends Annotation> extends DeclaredTest<ANNOTATION>
{
   protected AnnotationTest()
   {
      //noinspection unchecked
      super(() -> (ANNOTATION) SHADOW_API.getAnnotation("java.lang.annotation.Retention"));
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      assertTrue(SHADOW_API.getAnnotation("java.lang.Override").isSubtypeOf(SHADOW_API.getInterface("java.lang.annotation.Annotation")));
      assertTrue(SHADOW_API.getAnnotation("java.lang.Override").isSubtypeOf(SHADOW_API.getAnnotation("java.lang.Override")));
      assertFalse(SHADOW_API.getAnnotation("java.lang.Override").isSubtypeOf(SHADOW_API.getClass("java.lang.Number")));
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      assertEquals(List.of(SHADOW_API.getClass("java.lang.Object"), SHADOW_API.getInterface("java.lang.annotation.Annotation")),
                   SHADOW_API.getAnnotation("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.AnnotationNoParent")
                             .getDirectSuperTypes());
   }

   @Test
   @Override
   void testGetSuperTypes()
   {
      assertEquals(Set.of(SHADOW_API.getClass("java.lang.Object"), SHADOW_API.getInterface("java.lang.annotation.Annotation")),
                   SHADOW_API.getAnnotation("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.AnnotationNoParent")
                             .getSuperTypes());
   }
}
