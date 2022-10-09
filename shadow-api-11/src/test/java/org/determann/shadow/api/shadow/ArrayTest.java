package org.determann.shadow.api.shadow;

import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import javax.lang.model.type.TypeMirror;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayTest extends ShadowTest<Array>
{
   ArrayTest()
   {
      super(shadowApi -> shadowApi.convert(shadowApi.getClass("java.lang.String")).asArray());
   }

   @Test
   void isSubtypeOfTest()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 Declared string = shadowApi.getClass("java.lang.String");
                                 Array stringArray1 = shadowApi.convert(string).asArray();
                                 Array stringArray2 = shadowApi.convert(string).asArray();

                                 assertTrue(stringArray1.isSubtypeOf(stringArray2));

                                 Array collectionArray = shadowApi.convert(shadowApi.getInterface("java.util.Collection")).asArray();
                                 Array iterableArray = shadowApi.convert(shadowApi.getInterface("java.lang.Iterable")).asArray();
                                 assertFalse(collectionArray.isSubtypeOf(iterableArray));
                              })
                     .compile();
   }

   @Test
   void getComponentTypeTest()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 Declared string = shadowApi.getClass("java.lang.String");
                                 Array stringArray = shadowApi.convert(string).asArray();

                                 assertEquals(string, stringArray.getComponentType());
                              })
                     .compile();
   }

   @Test
   void getDirectSuperTypesTest()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 //declared array -> Object[]
                                 Declared string = shadowApi.getClass("java.lang.String");
                                 Array objectArray = shadowApi.convert(shadowApi.getClass("java.lang.Object")).asArray();
                                 Array stringArray = shadowApi.convert(string).asArray();

                                 List<Shadow<TypeMirror>> stringArraySupertypes = stringArray.getDirectSuperTypes();
                                 assertEquals(1, stringArraySupertypes.size());
                                 assertEquals(objectArray, stringArraySupertypes.get(0));

                                 //primitive array -> intersection of java.io.Serializable&java.lang.Cloneable
                                 Array intArray = shadowApi.convert(shadowApi.getConstants().getPrimitiveInt()).asArray();
                                 Declared serializable = shadowApi.getInterface("java.io.Serializable");
                                 Declared cloneable = shadowApi.getInterface("java.lang.Cloneable");
                                 List<Declared> primitiveArraySuper = List.of(serializable, cloneable);

                                 List<Shadow<TypeMirror>> directSupertypes = intArray.getDirectSuperTypes();
                                 assertEquals(1, directSupertypes.size());
                                 assertEquals(primitiveArraySuper, shadowApi.convert(directSupertypes.get(0)).toIntersection().getBounds());
                              })
                     .compile();
   }

   @Test
   void equalsTest()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 Declared string = shadowApi.getClass("java.lang.String");
                                 Array stringArray1 = shadowApi.convert(string).asArray();
                                 Array stringArray2 = shadowApi.convert(string).asArray();
                                 assertEquals(stringArray1, stringArray2);

                                 Array objectArray = shadowApi.convert(shadowApi.getClass("java.lang.Object")).asArray();
                                 assertNotEquals(stringArray1, objectArray);
                              })
                     .compile();
   }
}
