package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.internal.annotation_processing.dsl.QualifiedName;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.internal.annotation_processing.dsl.RenderingContextImpl.splitName;
import static org.junit.jupiter.api.Assertions.*;

class RenderingContextImplTest
{
   @Test
   void lowercaseType()
   {
      assertEquals(new QualifiedName("org.example", "myType"), splitName("org.example.myType"));
   }

   @Test
   void innerTypeNoPackage()
   {
      assertEquals(new QualifiedName(null, "Outer.Inner"), splitName("Outer.Inner"));
   }

   @Test
   void noPackage()
   {
      assertEquals(new QualifiedName(null, "MyType"), splitName("MyType"));
   }

   @Test
   void innerType()
   {
      assertEquals(new QualifiedName("org.example", "Outer.Inner"), splitName("org.example.Outer.Inner"));
   }

   @Test
   void simpleType()
   {
      assertEquals(new QualifiedName("org.example", "MyType"), splitName("org.example.MyType"));
   }

   @Test
   void multiDot()
   {
      assertDoesNotThrow(() -> splitName("..org.example...MyType.."));
   }

   @Test
   void illegal()
   {
      assertThrows(IllegalArgumentException.class, () -> splitName(""));
      assertThrows(IllegalArgumentException.class, () -> splitName("..."));
      assertThrows(NullPointerException.class, () -> splitName(null));
   }
}