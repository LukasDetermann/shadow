package io.determann.shadow.javadoc;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GenericUsageTest
{
   @Test
   void isSubtypeOf()
   {
      ProcessorTest.process(context ->
                            {
                               //@start region="GenericUsage.isSubtypeOf"
                               Ap.Interface interfaceToTest = context.getInterfaceOrThrow("java.util.List");
                               Ap.Interface erasure = interfaceToTest.erasure();
                               Ap.Interface erasedCollection = context.getInterfaceOrThrow("java.util.Collection").erasure();
                               Assertions.assertTrue(erasure.isSubtypeOf(erasedCollection));//@highlight substring="isSubtypeOf"
                               //@end
                            })
            .compile();
   }

   <T> void getGenerics()
   {
      //@start region="GenericUsage.getGenerics"
      List<T>//@highlight substring="T"
            //@end
            test = new ArrayList<>();
   }

   void getGenericTypes()
   {
      //@start region="GenericUsage.getGenericTypes"
      List<String>//@highlight substring="String"
            //@end
            test = new ArrayList<>();
   }
}
