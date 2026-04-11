package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.dsl.JavaDsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.test.ProcessorTest.processorTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class UnresolvedTest
{
   @Test
   void missingClass()
   {
      processorTest().withCodeToCompile("HasMissingClass.java", "public class HasMissingClass {Missing field;}")
                     .process(context ->
                              {
                                 Ap.Class hasMissingClass = context.getClassOrThrow("HasMissingClass");
                                 Ap.Field field = hasMissingClass.getFields().getFirst();
                                 Ap.VariableType fieldType = field.getType();

                                 if (context.isFirstRound())
                                 {
                                    assertInstanceOf(Ap.Unresolved.class, fieldType);
                                    Ap.Unresolved unresolved = (Ap.Unresolved) fieldType;
                                    assertEquals("Missing", unresolved.getName());

                                    context.writeAndCompileSourceFile(JavaDsl.class_().noPackage().name("Missing"));
                                 }
                                 else
                                 {
                                    assertInstanceOf(Ap.Class.class, fieldType);
                                 }
                              });
   }
}
