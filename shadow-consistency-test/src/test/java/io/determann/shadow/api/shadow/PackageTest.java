package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PackageTest
{
   @Test
   void testGetContent()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertTrue(shadowApi.getPackages("asdkfh").isEmpty());

                               assertEquals(List.of(shadowApi.getClassOrThrow(
                                                  "io.determann.shadow.example.processed.test.packagee.not_empty.AnyClass")),
                                            query(shadowApi.getPackages("io.determann.shadow.example.processed.test.packagee.not_empty")
                                                           .get(0))
                                                  .getContent());
                            })
                   .withCodeToCompile("AnyClass.java", """
                         package io.determann.shadow.example.processed.test.packagee.not_empty;

                         public class AnyClass {}""")
                   .compile();
   }
}
