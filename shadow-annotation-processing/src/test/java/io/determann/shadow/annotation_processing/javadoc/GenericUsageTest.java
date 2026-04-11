package io.determann.shadow.annotation_processing.javadoc;

import io.determann.shadow.api.annotation_processing.Ap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.api.annotation_processing.test.ProcessorTest.processorTest;

public class GenericUsageTest
{
   @Test
   void isSubtypeOf()
   {
      processorTest().process(context ->
                              {
                                 //@start region="GenericUsage.isSubtypeOf"
                                 Ap.Interface interfaceToTest = context.getInterfaceOrThrow("java.util.List");
                                 Ap.Interface erasure = interfaceToTest.erasure();
                                 Ap.Interface erasedCollection = context.getInterfaceOrThrow("java.util.Collection").erasure();
                                 Assertions.assertTrue(erasure.isSubtypeOf(erasedCollection));//@highlight substring="isSubtypeOf"
                                 //@end
                              });
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
