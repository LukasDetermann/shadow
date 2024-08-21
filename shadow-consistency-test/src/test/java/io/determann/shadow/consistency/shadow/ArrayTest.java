package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.type.LM_Array;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.lang_model.shadow.type.LM_Intersection;
import io.determann.shadow.api.lang_model.shadow.type.LM_Shadow;
import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Declared;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayTest extends ShadowTest<C_Array>
{
   ArrayTest()
   {
      super(context -> context.asArray(context.getClassOrThrow("java.lang.String")));
   }

   @Test
   void isSubtypeOfTest()
   {
      ProcessorTest.process(context ->
                            {
                               LM_Declared string = context.getClassOrThrow("java.lang.String");
                               LM_Array stringArray1 = context.asArray(string);
                               LM_Array stringArray2 = context.asArray(string);

                               assertTrue(stringArray1.isSubtypeOf(stringArray2));

                               LM_Array collectionArray = context.asArray(context.getInterfaceOrThrow("java.util.Collection"));
                               LM_Array iterableArray = context.asArray(context.getInterfaceOrThrow("java.lang.Iterable"));
                               assertFalse(collectionArray.isSubtypeOf(iterableArray));
                            })
                   .compile();
   }

   @Test
   void getComponentTypeTest()
   {
      ProcessorTest.process(context ->
                            {
                               LM_Declared string = context.getClassOrThrow("java.lang.String");
                               LM_Array stringArray = context.asArray(string);

                               assertEquals(string, stringArray.getComponentType());
                            })
                   .compile();
   }

   @Test
   void getDirectSuperTypesTest()
   {
      ProcessorTest.process(context ->
                            {
                               //declared array -> Object[]
                               LM_Declared string = context.getClassOrThrow("java.lang.String");
                               LM_Array objectArray = context.asArray(context.getClassOrThrow("java.lang.Object"));
                               LM_Array stringArray = context.asArray(string);

                               List<LM_Shadow> stringArraySupertypes = stringArray.getDirectSuperTypes();
                               assertEquals(1, stringArraySupertypes.size());
                               assertEquals(objectArray, stringArraySupertypes.get(0));

                               //primitive array -> intersection of java.io.Serializable&java.lang.Cloneable
                               LM_Array intArray = context.asArray(context.getConstants().getPrimitiveInt());
                               LM_Declared serializable = context.getInterfaceOrThrow("java.io.Serializable");
                               LM_Declared cloneable = context.getInterfaceOrThrow("java.lang.Cloneable");
                               List<LM_Declared> primitiveArraySuper = List.of(serializable, cloneable);

                               List<LM_Shadow> directSupertypes = intArray.getDirectSuperTypes();
                               assertEquals(1, directSupertypes.size());
                               assertEquals(primitiveArraySuper, ((LM_Intersection) directSupertypes.get(0)).getBounds());
                            })
                   .compile();
   }

   @Test
   void equalsTest()
   {
      ProcessorTest.process(context ->
                            {
                               C_Declared string = context.getClassOrThrow("java.lang.String");
                               C_Array stringArray1 = context.asArray(string);
                               C_Array stringArray2 = context.asArray(string);
                               assertEquals(stringArray1, stringArray2);

                               C_Array objectArray = context.asArray(context.getClassOrThrow("java.lang.Object"));
                               assertNotEquals(stringArray1, objectArray);
                            })
                   .compile();
   }
}
