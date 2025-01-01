package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.shadow.structure.C_Package;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.tck.internal.TckTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PackageTest
{
   @Test
   void testGetContent()
   {
      TckTest.withSource("AnyClass.java", """
                   package io.determann.shadow.example.processed.test.packagee.not_empty;
                   
                   public class AnyClass {}""")
             .test(implementation ->
                   {
                      assertTrue(requestOrThrow(implementation, GET_PACKAGES, "asdkfh").isEmpty());

                      C_Class anyClass = requestOrThrow(implementation,
                                                        GET_CLASS,
                                                        "io.determann.shadow.example.processed.test.packagee.not_empty.AnyClass");
                      C_Package cPackage = requestOrThrow(implementation,
                                                          GET_PACKAGES,
                                                          "io.determann.shadow.example.processed.test.packagee.not_empty").get(0);
                      assertEquals(List.of(anyClass), requestOrThrow(cPackage, PACKAGE_GET_DECLARED_LIST));
                   });
   }
}
