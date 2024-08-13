package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.shadow.type.Array;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.api.shadow.type.Intersection;
import io.determann.shadow.api.shadow.type.Shadow;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static org.junit.jupiter.api.Assertions.*;

class ArrayTest extends ShadowTest<Array>
{
   ArrayTest()
   {
      super(shadowApi -> shadowApi.asArray(shadowApi.getClassOrThrow("java.lang.String")));
   }

   @Test
   void isSubtypeOfTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Declared string = shadowApi.getClassOrThrow("java.lang.String");
                               Array stringArray1 = shadowApi.asArray(string);
                               Array stringArray2 = shadowApi.asArray(string);

                               assertTrue(query(stringArray1).isSubtypeOf(stringArray2));

                               Array collectionArray = shadowApi.asArray(shadowApi.getInterfaceOrThrow("java.util.Collection"));
                               Array iterableArray = shadowApi.asArray(shadowApi.getInterfaceOrThrow("java.lang.Iterable"));
                               assertFalse(query(collectionArray).isSubtypeOf(iterableArray));
                            })
                   .compile();
   }

   @Test
   void getComponentTypeTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Declared string = shadowApi.getClassOrThrow("java.lang.String");
                               Array stringArray = shadowApi.asArray(string);

                               assertEquals(string, query(stringArray).getComponentType());
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
                               Array objectArray = shadowApi.asArray(shadowApi.getClassOrThrow("java.lang.Object"));
                               Array stringArray = shadowApi.asArray(string);

                               List<Shadow> stringArraySupertypes = query(stringArray).getDirectSuperTypes();
                               assertEquals(1, stringArraySupertypes.size());
                               assertEquals(objectArray, stringArraySupertypes.get(0));

                               //primitive array -> intersection of java.io.Serializable&java.lang.Cloneable
                               Array intArray = shadowApi.asArray(shadowApi.getConstants().getPrimitiveInt());
                               Declared serializable = shadowApi.getInterfaceOrThrow("java.io.Serializable");
                               Declared cloneable = shadowApi.getInterfaceOrThrow("java.lang.Cloneable");
                               List<Declared> primitiveArraySuper = List.of(serializable, cloneable);

                               List<Shadow> directSupertypes = query(intArray).getDirectSuperTypes();
                               assertEquals(1, directSupertypes.size());
                               assertEquals(primitiveArraySuper, query(((Intersection) directSupertypes.get(0))).getBounds());
                            })
                   .compile();
   }

   @Test
   void equalsTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Declared string = shadowApi.getClassOrThrow("java.lang.String");
                               Array stringArray1 = shadowApi.asArray(string);
                               Array stringArray2 = shadowApi.asArray(string);
                               assertEquals(stringArray1, stringArray2);

                               Array objectArray = shadowApi.asArray(shadowApi.getClassOrThrow("java.lang.Object"));
                               assertNotEquals(stringArray1, objectArray);
                            })
                   .compile();
   }
}
