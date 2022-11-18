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
      super(shadowApi -> shadowApi.getPackageOrThrow("java.base", "java.lang"));
   }

   @Test
   void testGetContent()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertTrue(shadowApi.getPackagesOrThrow("asdkfh").isEmpty());

                                 assertEquals(List.of(shadowApi.getClassOrThrow(
                                                    "org.determann.shadow.example.processed.test.packagee.not_empty.AnyClass")),
                                              shadowApi.getPackagesOrThrow("org.determann.shadow.example.processed.test.packagee.not_empty")
                                                       .get(0)
                                                       .getContent());
                              })
                     .withCodeToCompile("AnyClass.java", "package org.determann.shadow.example.processed.test.packagee.not_empty;\n" +
                                                         "\n" +
                                                         "                           public class AnyClass {}")
                     .compile();
   }
}
