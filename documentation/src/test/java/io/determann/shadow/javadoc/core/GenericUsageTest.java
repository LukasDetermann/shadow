package io.determann.shadow.javadoc.core;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.shadow.Interface;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;

public class GenericUsageTest
{
   @Test
   void isSubtypeOf()
   {
      ProcessorTest.process(context ->
                            {
                               //@start region="GenericUsage.isSubtypeOf"
                               Interface interfaceToTest = context.getInterfaceOrThrow("java.util.List");
                               Interface erasure = context.erasure(interfaceToTest);
                               Interface erasedCollection = context.erasure(context.getInterfaceOrThrow("java.util.Collection"));
                               Assertions.assertTrue(query(erasure).isSubtypeOf(erasedCollection));//@highlight substring="isSubtypeOf"
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
