package io.determann.shadow.tck.internal.shadow.type;

import io.determann.shadow.api.shadow.type.C_Annotation;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.tck.internal.TckTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.*;

class AnnotationTest
{
   @Test
   void isSubtypeOf()
   {
      TckTest.test(implementation ->
                   {
                      C_Annotation overwrite = requestOrThrow(implementation, GET_ANNOTATION, "java.lang.Override");
                      C_Interface annotation = requestOrThrow(implementation, GET_INTERFACE, "java.lang.annotation.Annotation");
                      C_Class number = requestOrThrow(implementation, GET_CLASS, "java.lang.Number");

                      assertTrue(requestOrThrow(overwrite, DECLARED_IS_SUBTYPE_OF, annotation));
                      assertTrue(requestOrThrow(overwrite, DECLARED_IS_SUBTYPE_OF, overwrite));
                      assertFalse(requestOrThrow(overwrite, DECLARED_IS_SUBTYPE_OF, number));
                   });
   }

   @Test
   void getDirectSuperTypes()
   {
      withSource("DirektSuperTypeExample.java", """
            public class DirektSuperTypeExample {
               @interface AnnotationNoParent {}
            }
            """)
            .test(implementation ->
                  {
                     List<? extends C_Declared> expected = List.of(requestOrThrow(implementation, GET_CLASS, "java.lang.Object"),
                                                                   requestOrThrow(implementation,
                                                                                  GET_INTERFACE,
                                                                                  "java.lang.annotation.Annotation"));

                     C_Annotation annotation = requestOrThrow(implementation, GET_ANNOTATION, "DirektSuperTypeExample.AnnotationNoParent");
                     assertEquals(expected, requestOrThrow(annotation, DECLARED_GET_DIRECT_SUPER_TYPES));
                  });
   }

   @Test
   void getSuperTypes()
   {
      withSource("DirektSuperTypeExample.java", """
            public class DirektSuperTypeExample {
               @interface AnnotationNoParent {}
            }
            """)
            .test(implementation ->
                  {
                     Set<? extends C_Declared> expected = Set.of(requestOrThrow(implementation, GET_CLASS, "java.lang.Object"),
                                                                 requestOrThrow(implementation,
                                                                                GET_INTERFACE,
                                                                                "java.lang.annotation.Annotation"));

                     C_Annotation annotation = requestOrThrow(implementation, GET_ANNOTATION, "DirektSuperTypeExample.AnnotationNoParent");
                     assertEquals(expected, requestOrThrow(annotation, DECLARED_GET_SUPER_TYPES));
                  });
   }
}
