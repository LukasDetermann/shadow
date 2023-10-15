package io.determann.shadow.api.shadow;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.converter.Converter.convert;
import static org.junit.jupiter.api.Assertions.*;

class ArrayTest extends ShadowTest<Array>
{
   ArrayTest()
   {
      super(shadowApi -> shadowApi.getClassOrThrow("java.lang.String").asArray());
   }

   @Test
   void isSubtypeOfTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Declared string = shadowApi.getClassOrThrow("java.lang.String");
                               Array stringArray1 = string.asArray();
                               Array stringArray2 = string.asArray();

                               assertTrue(stringArray1.isSubtypeOf(stringArray2));

                               Array collectionArray = shadowApi.getInterfaceOrThrow("java.util.Collection").asArray();
                               Array iterableArray = shadowApi.getInterfaceOrThrow("java.lang.Iterable").asArray();
                               assertFalse(collectionArray.isSubtypeOf(iterableArray));
                            })
                   .compile();
   }

   @Test
   void getComponentTypeTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Declared string = shadowApi.getClassOrThrow("java.lang.String");
                               Array stringArray = string.asArray();

                               assertEquals(string, stringArray.getComponentType());
                            })
                   .compile();
   }

   @Test
   void getDirectSuperTypesTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               //declared array -> Object[]
                               Declared string = shadowApi.getClassOrThrow("java.lang.String");
                               Array objectArray = shadowApi.getClassOrThrow("java.lang.Object").asArray();
                               Array stringArray = string.asArray();

                               List<Shadow> stringArraySupertypes = stringArray.getDirectSuperTypes();
                               assertEquals(1, stringArraySupertypes.size());
                               assertEquals(objectArray, stringArraySupertypes.get(0));

                               //primitive array -> intersection of java.io.Serializable&java.lang.Cloneable
                               Array intArray = shadowApi.getConstants().getPrimitiveInt().asArray();
                               Declared serializable = shadowApi.getInterfaceOrThrow("java.io.Serializable");
                               Declared cloneable = shadowApi.getInterfaceOrThrow("java.lang.Cloneable");
                               List<Declared> primitiveArraySuper = List.of(serializable, cloneable);

                               List<Shadow> directSupertypes = intArray.getDirectSuperTypes();
                               assertEquals(1, directSupertypes.size());
                               assertEquals(primitiveArraySuper, convert(directSupertypes.get(0)).toIntersectionOrThrow().getBounds());
                            })
                   .compile();
   }

   @Test
   void equalsTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Declared string = shadowApi.getClassOrThrow("java.lang.String");
                               Array stringArray1 = string.asArray();
                               Array stringArray2 = string.asArray();
                               assertEquals(stringArray1, stringArray2);

                               Array objectArray = shadowApi.getClassOrThrow("java.lang.Object").asArray();
                               assertNotEquals(stringArray1, objectArray);
                            })
                   .compile();
   }
}
