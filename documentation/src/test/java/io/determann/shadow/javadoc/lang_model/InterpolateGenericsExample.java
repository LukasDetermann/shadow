package io.determann.shadow.javadoc.lang_model;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.type.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class InterpolateGenericsExample
{
   @Test
   void interpolateGenerics()
   {
      ProcessorTest.process(context ->
                            {
                               //@start region="InterpolateGenerics.interpolateGenerics"
                               LM_Class myClass = context.getClassOrThrow("MyClass");
                               LM_Declared string = context.getDeclaredOrThrow("java.lang.String");

                               LM_Class withGenerics = context.withGenerics(myClass,
                                                                            string,
                                                                            //the unboundWildcard will be replaced with the result
                                                                            context.getConstants().getUnboundWildcard());

                               LM_Class capture = context.interpolateGenerics(withGenerics);

                               LM_Shadow stringRep = Optional.of(capture.getGenericTypes().get(1))
                                                             .map(LM_Generic.class::cast)
                                                             .map(LM_Generic::getExtends)
                                                             .map(LM_Interface.class::cast)
                                                             .map(LM_Interface::getGenericTypes)
                                                             .map(shadows -> shadows.get(0))
                                                             .orElseThrow();

                               Assertions.assertEquals(string, stringRep);
                               //@end
                            })
                   .withCodeToCompile("MyClass.java",
                                      //@start region="InterpolateGenerics.interpolateGenerics.code"
                                      "public class MyClass<A extends Comparable<B>, B extends Comparable<A>> {}"
                                      //@end
                                     )
                   .compile();
   }
}
