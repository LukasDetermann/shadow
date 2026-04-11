package io.determann.shadow.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.annotation_processing.test.ProcessorTest.processorTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PackageTest
{
   @Test
   void testGetContent()
   {
      processorTest().withCodeToCompile("AnyClass.java", """
                                                         package io.determann.shadow.example.processed.test.packagee.not_empty;
                                                         
                                                         public class AnyClass {}""")
                     .process(context ->
                              {
                                 assertTrue(context.getPackage("asdkfh").isEmpty());

                                 Ap.Class anyClass = context.getClassOrThrow(
                                       "io.determann.shadow.example.processed.test.packagee.not_empty.AnyClass");

                                 Ap.Package cPackage = context.getPackage("io.determann.shadow.example.processed.test.packagee.not_empty")
                                                              .get(0);

                                 assertEquals(List.of(anyClass), cPackage.getDeclared());
                              });
   }
}
