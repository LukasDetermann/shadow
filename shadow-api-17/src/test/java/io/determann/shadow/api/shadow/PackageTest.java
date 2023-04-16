package io.determann.shadow.api.shadow;

import io.determann.shadow.api.test.CompilationTest;
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
                                                    "io.determann.shadow.example.processed.test.packagee.not_empty.AnyClass")),
                                              shadowApi.getPackagesOrThrow("io.determann.shadow.example.processed.test.packagee.not_empty")
                                                       .get(0)
                                                       .getContent());
                              })
                     .withCodeToCompile("AnyClass.java", """
                           package io.determann.shadow.example.processed.test.packagee.not_empty;

                           public class AnyClass {}""")
                     .compile();
   }
}