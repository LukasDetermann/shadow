package io.determann.shadow.consistency.shadow.type;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.type.LM_Array;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.lang_model.shadow.type.LM_Intersection;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.shadow.type.C_Array;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayTest
{
   @Test
   void isSubtypeOfTest()
   {
      ProcessorTest.process(context ->
                            {
                               LM_Declared string = context.getClassOrThrow("java.lang.String");
                               LM_Array stringArray1 = string.asArray();
                               LM_Array stringArray2 = string.asArray();

                               assertTrue(stringArray1.isSubtypeOf(stringArray2));

                               LM_Array collectionArray = context.getInterfaceOrThrow("java.util.Collection").asArray();
                               LM_Array iterableArray = context.getInterfaceOrThrow("java.lang.Iterable").asArray();
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
                               LM_Array stringArray = string.asArray();

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
                               LM_Array objectArray = context.getClassOrThrow("java.lang.Object").asArray();
                               LM_Array stringArray = string.asArray();

                               List<LM_Type> stringArraySupertypes = stringArray.getDirectSuperTypes();
                               assertEquals(1, stringArraySupertypes.size());
                               assertEquals(objectArray, stringArraySupertypes.get(0));

                               //primitive array -> intersection of java.io.Serializable&java.lang.Cloneable
                               LM_Array intArray = context.getConstants().getPrimitiveInt().asArray();
                               LM_Declared serializable = context.getInterfaceOrThrow("java.io.Serializable");
                               LM_Declared cloneable = context.getInterfaceOrThrow("java.lang.Cloneable");
                               List<LM_Declared> primitiveArraySuper = List.of(serializable, cloneable);

                               List<LM_Type> directSupertypes = intArray.getDirectSuperTypes();
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
                               LM_Declared string = context.getClassOrThrow("java.lang.String");
                               C_Array stringArray1 = string.asArray();
                               C_Array stringArray2 = string.asArray();
                               assertEquals(stringArray1, stringArray2);

                               C_Array objectArray = context.getClassOrThrow("java.lang.Object").asArray();
                               assertNotEquals(stringArray1, objectArray);
                            })
                   .compile();
   }
}
