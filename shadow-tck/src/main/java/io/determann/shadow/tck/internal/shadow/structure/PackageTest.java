package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.tck.internal.TckTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
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
                      assertTrue(requestOrThrow(implementation, GET_PACKAGE, "asdkfh").isEmpty());

                      C.Class anyClass = requestOrThrow(implementation,
                                                        GET_CLASS,
                                                        "io.determann.shadow.example.processed.test.packagee.not_empty.AnyClass");
                      C.Package cPackage = requestOrThrow(implementation,
                                                          GET_PACKAGE,
                                                          "io.determann.shadow.example.processed.test.packagee.not_empty").get(0);
                      assertEquals(List.of(anyClass), requestOrThrow(cPackage, PACKAGE_GET_DECLARED_LIST));
                   });
   }
}
