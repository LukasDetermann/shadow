package org.determann.shadow.api.shadow;

import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PackageTest extends ShadowTest<Package>
{
   PackageTest()
   {
      super(shadowApi -> shadowApi.getPackage("java.base", "java.lang"));
   }

   @Test
   void testGetContent()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertTrue(shadowApi.getPackages("asdkfh").isEmpty());

                                 assertEquals(List.of(shadowApi.getClass(
                                                    "org.determann.shadow.example.processed.test.packagee.not_empty.AnyClass")),
                                              shadowApi.getPackages("org.determann.shadow.example.processed.test.packagee.not_empty")
                                                       .get(0)
                                                       .getContent());
                              })
                     .withCodeToCompile("AnyClass.java", "package org.determann.shadow.example.processed.test.packagee.not_empty;\n" +
                                                         "\n" +
                                                         "                           public class AnyClass {}")
                     .compile();
   }
}
