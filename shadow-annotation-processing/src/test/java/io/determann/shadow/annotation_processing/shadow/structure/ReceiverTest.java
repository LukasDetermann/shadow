package io.determann.shadow.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.annotation_processing.test.ProcessorTest.processorTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceiverTest
{
   @Test
   void getType()
   {
      processorTest().withCodeToCompile("ReceiverExample.java", """
                                                                public class ReceiverExample {
                                                                   public class Inner {
                                                                      public Inner(ReceiverExample ReceiverExample.this) {}
                                                                   }
                                                                }
                                                                """)
                     .process(context ->
                              {
                                 Ap.Class outer = context.getClassOrThrow("ReceiverExample");
                                 Ap.Class inner = context.getClassOrThrow("ReceiverExample.Inner");
                                 Ap.Constructor cConstructor = inner.getConstructors().get(0);
                                 Ap.Receiver cReceiver = cConstructor.getReceiverOrThrow();

                                 assertEquals(outer, cReceiver.getType());
                              });
   }

   @Test
   void getAnnotationUsages()
   {
      processorTest().withCodeToCompile("MyAnnotation.java",
                                        "@java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE_USE) @interface MyAnnotation{int value();}")
                     .withCodeToCompile("ReceiverExample.java", """
                                                                public class ReceiverExample {
                                                                   public class Inner {
                                                                      public Inner(@MyAnnotation(2) ReceiverExample ReceiverExample.this) {}
                                                                   }
                                                                }
                                                                """)
                     .process(context ->
                              {
                                 Ap.Class outer = context.getClassOrThrow("ReceiverExample");
                                 Ap.Class inner = context.getClassOrThrow("ReceiverExample.Inner");
                                 Ap.Constructor cConstructor = inner.getConstructors().get(0);
                                 Ap.Receiver cReceiver = cConstructor.getReceiverOrThrow();

                                 assertEquals(outer, cReceiver.getType());

                                 List<Ap.AnnotationUsage> usages = cReceiver.getAnnotationUsages();
                                 assertEquals(1, usages.size());
                                 Ap.AnnotationValue annotationValue = usages.get(0).getValueOrThrow("value");
                                 assertEquals(2, annotationValue.getValue());
                              });
   }
}