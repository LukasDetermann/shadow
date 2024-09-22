package io.determann.shadow.consistency.shadow.structure;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Receiver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceiverTest
{
   @Test
   void getType()
   {
      ProcessorTest.process(context ->
                            {
                               LM_Receiver receiver = context.getClassOrThrow("ReceiverExample.Inner")
                                                             .getConstructors()
                                                             .get(0)
                                                             .getReceiver()
                                                             .get();

                               assertEquals(context.getClassOrThrow("ReceiverExample"), receiver.getType());
                            })
                   .withCodeToCompile("ReceiverExample.java", """
                         public class ReceiverExample {
                            public class Inner {
                               public Inner(ReceiverExample ReceiverExample.this) {}
                            }
                         }
                         """)
                   .compile();
   }

   @Test
   void getAnnotationUsages()
   {
      ProcessorTest.process(context ->
                            {
                               LM_Receiver receiver = context.getClassOrThrow("ReceiverExample.Inner")
                                                             .getConstructors()
                                                             .get(0)
                                                             .getReceiver()
                                                             .get();

                               assertEquals(1, receiver.getAnnotationUsages().size());
                               assertEquals(2, receiver.getAnnotationUsages().get(0).getValueOrThrow("value").getValue());
                            })
                   .withCodeToCompile("MyAnnotation.java", "@java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE_USE) @interface MyAnnotation{int value();}")
                   .withCodeToCompile("ReceiverExample.java", """
                         public class ReceiverExample {
                            public class Inner {
                               public Inner(@MyAnnotation(2) ReceiverExample ReceiverExample.this) {}
                            }
                         }
                         """)
                   .compile();
   }
}