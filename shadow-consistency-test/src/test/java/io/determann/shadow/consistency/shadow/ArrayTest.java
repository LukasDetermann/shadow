package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.type.ArrayLangModel;
import io.determann.shadow.api.lang_model.shadow.type.DeclaredLangModel;
import io.determann.shadow.api.lang_model.shadow.type.IntersectionLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;
import io.determann.shadow.api.shadow.type.Array;
import io.determann.shadow.api.shadow.type.Declared;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayTest extends ShadowTest<Array>
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
                               DeclaredLangModel string = context.getClassOrThrow("java.lang.String");
                               ArrayLangModel stringArray1 = context.asArray(string);
                               ArrayLangModel stringArray2 = context.asArray(string);

                               assertTrue(stringArray1.isSubtypeOf(stringArray2));

                               ArrayLangModel collectionArray = context.asArray(context.getInterfaceOrThrow("java.util.Collection"));
                               ArrayLangModel iterableArray = context.asArray(context.getInterfaceOrThrow("java.lang.Iterable"));
                               assertFalse(collectionArray.isSubtypeOf(iterableArray));
                            })
                   .compile();
   }

   @Test
   void getComponentTypeTest()
   {
      ProcessorTest.process(context ->
                            {
                               DeclaredLangModel string = context.getClassOrThrow("java.lang.String");
                               ArrayLangModel stringArray = context.asArray(string);

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
                               DeclaredLangModel string = context.getClassOrThrow("java.lang.String");
                               ArrayLangModel objectArray = context.asArray(context.getClassOrThrow("java.lang.Object"));
                               ArrayLangModel stringArray = context.asArray(string);

                               List<ShadowLangModel> stringArraySupertypes = stringArray.getDirectSuperTypes();
                               assertEquals(1, stringArraySupertypes.size());
                               assertEquals(objectArray, stringArraySupertypes.get(0));

                               //primitive array -> intersection of java.io.Serializable&java.lang.Cloneable
                               ArrayLangModel intArray = context.asArray(context.getConstants().getPrimitiveInt());
                               DeclaredLangModel serializable = context.getInterfaceOrThrow("java.io.Serializable");
                               DeclaredLangModel cloneable = context.getInterfaceOrThrow("java.lang.Cloneable");
                               List<DeclaredLangModel> primitiveArraySuper = List.of(serializable, cloneable);

                               List<ShadowLangModel> directSupertypes = intArray.getDirectSuperTypes();
                               assertEquals(1, directSupertypes.size());
                               assertEquals(primitiveArraySuper, ((IntersectionLangModel) directSupertypes.get(0)).getBounds());
                            })
                   .compile();
   }

   @Test
   void equalsTest()
   {
      ProcessorTest.process(context ->
                            {
                               Declared string = context.getClassOrThrow("java.lang.String");
                               Array stringArray1 = context.asArray(string);
                               Array stringArray2 = context.asArray(string);
                               assertEquals(stringArray1, stringArray2);

                               Array objectArray = context.asArray(context.getClassOrThrow("java.lang.Object"));
                               assertNotEquals(stringArray1, objectArray);
                            })
                   .compile();
   }
}
