package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Array;
import org.determann.shadow.api.shadow.Declared;
import org.determann.shadow.api.shadow.Shadow;
import org.junit.jupiter.api.Test;

import javax.lang.model.type.TypeMirror;
import java.util.List;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayTest
{
   @Test
   void isSubtypeOfTest()
   {
      Declared string = SHADOW_API.getClass("java.lang.String");
      Array stringArray1 = SHADOW_API.convert(string).asArray();
      Array stringArray2 = SHADOW_API.convert(string).asArray();

      assertTrue(stringArray1.isSubtypeOf(stringArray2));

      Array collectionArray = SHADOW_API.convert(SHADOW_API.getInterface("java.util.Collection")).asArray();
      Array iterableArray = SHADOW_API.convert(SHADOW_API.getInterface("java.lang.Iterable")).asArray();
      assertFalse(collectionArray.isSubtypeOf(iterableArray));
   }

   @Test
   void getComponentTypeTest()
   {
      Declared string = SHADOW_API.getClass("java.lang.String");
      Array stringArray = SHADOW_API.convert(string).asArray();

      assertEquals(string, stringArray.getComponentType());
   }

   @Test
   void getDirectSuperTypesTest()
   {
      //declared array -> Object[]
      Declared string = SHADOW_API.getClass("java.lang.String");
      Array objectArray = SHADOW_API.convert(SHADOW_API.getClass("java.lang.Object")).asArray();
      Array stringArray = SHADOW_API.convert(string).asArray();

      List<Shadow<TypeMirror>> stringArraySupertypes = stringArray.getDirectSuperTypes();
      assertEquals(1, stringArraySupertypes.size());
      assertEquals(objectArray, stringArraySupertypes.get(0));

      //primitive array -> intersection of java.io.Serializable&java.lang.Cloneable
      Array intArray = SHADOW_API.convert(SHADOW_API.getConstants().getPrimitiveInt()).asArray();
      Declared serializable = SHADOW_API.getInterface("java.io.Serializable");
      Declared cloneable = SHADOW_API.getInterface("java.lang.Cloneable");
      List<Declared> primitiveArraySuper = List.of(serializable, cloneable);

      List<Shadow<TypeMirror>> directSupertypes = intArray.getDirectSuperTypes();
      assertEquals(1, directSupertypes.size());
      assertEquals(primitiveArraySuper, SHADOW_API.convert(directSupertypes.get(0)).toIntersection().orElseThrow().getBounds());
   }

   @Test
   void equalsTest()
   {
      Declared string = SHADOW_API.getClass("java.lang.String");
      Array stringArray1 = SHADOW_API.convert(string).asArray();
      Array stringArray2 = SHADOW_API.convert(string).asArray();
      assertTrue(stringArray1.equals(stringArray2));

      Array objectArray = SHADOW_API.convert(SHADOW_API.getClass("java.lang.Object")).asArray();
      assertFalse(stringArray1.equals(objectArray));
   }
}
