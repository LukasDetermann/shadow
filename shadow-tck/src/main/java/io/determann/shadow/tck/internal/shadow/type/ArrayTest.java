package io.determann.shadow.tck.internal.shadow.type;

import io.determann.shadow.api.shadow.type.*;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.test;
import static org.junit.jupiter.api.Assertions.*;

class ArrayTest
{
   @Test
   void isSubtypeOfTest()
   {
      test(implementation ->
           {
              C_Class string = requestOrThrow(implementation, GET_CLASS, "java.lang.String");
              C_Array stringArray1 = requestOrThrow(string, DECLARED_AS_ARRAY);
              C_Array stringArray2 = requestOrThrow(string, DECLARED_AS_ARRAY);

              assertTrue(requestOrThrow(stringArray1, ARRAY_IS_SUBTYPE_OF, stringArray2));

              C_Interface collection = requestOrThrow(implementation, GET_INTERFACE, "java.util.Collection");
              C_Array collectionArray = requestOrThrow(collection, DECLARED_AS_ARRAY);
              C_Interface iterable = requestOrThrow(implementation, GET_INTERFACE, "java.lang.Iterable");
              C_Array iterableArray = requestOrThrow(iterable, DECLARED_AS_ARRAY);
              assertFalse(requestOrThrow(collectionArray, ARRAY_IS_SUBTYPE_OF, iterableArray));
           });
   }

   @Test
   void getComponentTypeTest()
   {
      test(implementation ->
           {
              C_Class string = requestOrThrow(implementation, GET_CLASS, "java.lang.String");
              C_Array stringArray = requestOrThrow(string, DECLARED_AS_ARRAY);
              assertEquals(string, requestOrThrow(stringArray, ARRAY_GET_COMPONENT_TYPE));
           });
   }

   @Test
   void getDirectSuperTypesTest()
   {
      test(implementation ->
           {
              C_Class string = requestOrThrow(implementation, GET_CLASS, "java.lang.String");
              C_Class object = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");
              C_Array stringArray = requestOrThrow(string, DECLARED_AS_ARRAY);
              C_Array objectArray = requestOrThrow(object, DECLARED_AS_ARRAY);

              List<? extends C_Type> superTypes = requestOrThrow(stringArray, ARRAY_GET_DIRECT_SUPER_TYPES);
              assertEquals(1, superTypes.size());
              assertEquals(objectArray, superTypes.get(0));

              C_Primitive cint = requestOrThrow(implementation, GET_INT);
              C_Array intArray = requestOrThrow(cint, PRIMITIVE_AS_ARRAY);
              C_Interface serializable = requestOrThrow(implementation, GET_INTERFACE, "java.io.Serializable");
              C_Interface cloneable = requestOrThrow(implementation, GET_INTERFACE, "java.lang.Cloneable");

              List<? extends C_Type> intArraySuperTypes = requestOrThrow(intArray, ARRAY_GET_DIRECT_SUPER_TYPES);
              assertEquals(1, intArraySuperTypes.size());
              List<? extends C_Type> bounds = requestOrThrow(((C_Generic) intArraySuperTypes.get(0)), GENERIC_GET_BOUNDS);
              assertEquals(List.of(serializable, cloneable), bounds);
           });
   }
}
