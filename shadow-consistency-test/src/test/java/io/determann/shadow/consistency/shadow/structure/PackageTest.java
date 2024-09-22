package io.determann.shadow.consistency.shadow.structure;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PackageTest
{
   @Test
   void testGetContent()
   {
      ProcessorTest.process(context ->
                            {
                               assertTrue(context.getPackages("asdkfh").isEmpty());

                               assertEquals(List.of(context.getClassOrThrow(
                                                  "io.determann.shadow.example.processed.test.packagee.not_empty.AnyClass")),
                                            context.getPackages("io.determann.shadow.example.processed.test.packagee.not_empty")
                                                           .get(0)
                                                  .getDeclared());
                            })
                   .withCodeToCompile("AnyClass.java", """
                         package io.determann.shadow.example.processed.test.packagee.not_empty;

                         public class AnyClass {}""")
                   .compile();
   }
}
