package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.C_AnnotationValue;
import io.determann.shadow.api.shadow.structure.C_Constructor;
import io.determann.shadow.api.shadow.structure.C_Receiver;
import io.determann.shadow.api.shadow.type.C_Class;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceiverTest
{
   @Test
   void getType()
   {
      withSource("ReceiverExample.java", """
            public class ReceiverExample {
               public class Inner {
                  public Inner(ReceiverExample ReceiverExample.this) {}
               }
            }
            """)
            .test(implementation ->
                  {
                     C_Class outer = requestOrThrow(implementation, GET_CLASS, "ReceiverExample");
                     C_Class inner = requestOrThrow(implementation, GET_CLASS, "ReceiverExample.Inner");
                     C_Constructor cConstructor = requestOrThrow(inner, CLASS_GET_CONSTRUCTORS).get(0);
                     C_Receiver cReceiver = requestOrThrow(cConstructor, EXECUTABLE_GET_RECEIVER);

                     assertEquals(outer, requestOrThrow(cReceiver, RECEIVER_GET_TYPE));
                  });
   }

   @Test
   void getAnnotationUsages()
   {
      withSource("MyAnnotation.java",
                 "@java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE_USE) @interface MyAnnotation{int value();}")
            .withSource("ReceiverExample.java", """
                  public class ReceiverExample {
                     public class Inner {
                        public Inner(@MyAnnotation(2) ReceiverExample ReceiverExample.this) {}
                     }
                  }
                  """)
            .test(implementation ->
                  {
                     C_Class outer = requestOrThrow(implementation, GET_CLASS, "ReceiverExample");
                     C_Class inner = requestOrThrow(implementation, GET_CLASS, "ReceiverExample.Inner");
                     C_Constructor cConstructor = requestOrThrow(inner, CLASS_GET_CONSTRUCTORS).get(0);
                     C_Receiver cReceiver = requestOrThrow(cConstructor, EXECUTABLE_GET_RECEIVER);

                     assertEquals(outer, requestOrThrow(cReceiver, RECEIVER_GET_TYPE));

                     List<? extends C_AnnotationUsage> usages = requestOrThrow(cReceiver, ANNOTATIONABLE_GET_ANNOTATION_USAGES);
                     assertEquals(1, usages.size());
                     C_AnnotationValue annotationValue = requestOrThrow(usages.get(0), ANNOTATION_USAGE_GET_VALUE, "value");
                     assertEquals(2, requestOrThrow(annotationValue, ANNOTATION_VALUE_GET_VALUE));
                  });
   }
}