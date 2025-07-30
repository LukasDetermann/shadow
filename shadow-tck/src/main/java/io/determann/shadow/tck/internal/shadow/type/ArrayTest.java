package io.determann.shadow.tck.internal.shadow.type;

import io.determann.shadow.api.C;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.test;
import static org.junit.jupiter.api.Assertions.*;

class ArrayTest
{
   @Test
   void isSubtypeOfTest()
   {
      test(implementation ->
           {
              C.Class string = requestOrThrow(implementation, GET_CLASS, "java.lang.String");
              C.Array stringArray1 = requestOrThrow(string, DECLARED_AS_ARRAY);
              C.Array stringArray2 = requestOrThrow(string, DECLARED_AS_ARRAY);

              assertTrue(requestOrThrow(stringArray1, ARRAY_IS_SUBTYPE_OF, stringArray2));

              C.Interface collection = requestOrThrow(implementation, GET_INTERFACE, "java.util.Collection");
              C.Array collectionArray = requestOrThrow(collection, DECLARED_AS_ARRAY);
              C.Interface iterable = requestOrThrow(implementation, GET_INTERFACE, "java.lang.Iterable");
              C.Array iterableArray = requestOrThrow(iterable, DECLARED_AS_ARRAY);
              assertFalse(requestOrThrow(collectionArray, ARRAY_IS_SUBTYPE_OF, iterableArray));
           });
   }

   @Test
   void getComponentTypeTest()
   {
      test(implementation ->
           {
              C.Class string = requestOrThrow(implementation, GET_CLASS, "java.lang.String");
              C.Array stringArray = requestOrThrow(string, DECLARED_AS_ARRAY);
              assertEquals(string, requestOrThrow(stringArray, ARRAY_GET_COMPONENT_TYPE));
           });
   }

   @Test
   void getDirectSuperTypesTest()
   {
      test(implementation ->
           {
              C.Class string = requestOrThrow(implementation, GET_CLASS, "java.lang.String");
              C.Class object = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");
              C.Array stringArray = requestOrThrow(string, DECLARED_AS_ARRAY);
              C.Array objectArray = requestOrThrow(object, DECLARED_AS_ARRAY);

              List<? extends C.Type> superTypes = requestOrThrow(stringArray, ARRAY_GET_DIRECT_SUPER_TYPES);
              assertEquals(1, superTypes.size());
              assertEquals(objectArray, superTypes.get(0));

              C.Primitive cint = requestOrThrow(implementation, GET_INT);
              C.Array intArray = requestOrThrow(cint, PRIMITIVE_AS_ARRAY);
              C.Interface serializable = requestOrThrow(implementation, GET_INTERFACE, "java.io.Serializable");
              C.Interface cloneable = requestOrThrow(implementation, GET_INTERFACE, "java.lang.Cloneable");

              List<? extends C.Type> intArraySuperTypes = requestOrThrow(intArray, ARRAY_GET_DIRECT_SUPER_TYPES);
              assertEquals(1, intArraySuperTypes.size());
              List<? extends C.Type> bounds = requestOrThrow(((C.Generic) intArraySuperTypes.get(0)), GENERIC_GET_BOUNDS);
              assertEquals(List.of(serializable, cloneable), bounds);
           });
   }
}
