package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
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
                               Ap.Class string = context.getClassOrThrow("java.lang.String");
                               Ap.Array stringArray1 = string.asArray();
                               Ap.Array stringArray2 = string.asArray();

                               assertTrue(stringArray1.isSubtypeOf(stringArray2));

                               Ap.Interface collection = context.getInterfaceOrThrow("java.util.Collection");
                               Ap.Array collectionArray = collection.asArray();
                               Ap.Interface iterable = context.getInterfaceOrThrow("java.lang.Iterable");
                               Ap.Array iterableArray = iterable.asArray();
                               assertFalse(collectionArray.isSubtypeOf(iterableArray));
                            })
                   .compile();
   }

   @Test
   void getComponentTypeTest()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class string = context.getClassOrThrow("java.lang.String");
                               Ap.Array stringArray = string.asArray();
                               assertEquals(string, stringArray.getComponentType());
                            })
                   .compile();
   }

   @Test
   void getDirectSuperTypesTest()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class string = context.getClassOrThrow("java.lang.String");
                               Ap.Class object = context.getClassOrThrow("java.lang.Object");
                               Ap.Array stringArray = string.asArray();
                               Ap.Array objectArray = object.asArray();

                               List<Ap.Type> superTypes = stringArray.getDirectSuperTypes();
                               assertEquals(1, superTypes.size());
                               assertEquals(objectArray, superTypes.get(0));

                               Ap.Primitive cint = context.getConstants().getPrimitiveInt();

                               Ap.Array intArray = cint.asArray();
                               Ap.Interface serializable = context.getInterfaceOrThrow("java.io.Serializable");
                               Ap.Interface cloneable = context.getInterfaceOrThrow("java.lang.Cloneable");

                               List<Ap.Type> intArraySuperTypes = intArray.getDirectSuperTypes();
                               assertEquals(1, intArraySuperTypes.size());
                               List<Ap.Type> bounds = ((Ap.Generic) intArraySuperTypes.get(0)).getBounds();
                               assertEquals(List.of(serializable, cloneable), bounds);
                            })
                   .compile();
   }
}
