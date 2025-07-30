package io.determann.shadow.javadoc;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.LM;
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
                               LM.Class myClass = context.getClassOrThrow("MyClass");
                               LM.Declared string = context.getDeclaredOrThrow("java.lang.String");

                               LM.Class withGenerics = myClass.withGenerics(string,
                                                                            //the unboundWildcard will be replaced with the result
                                                                            context.getConstants().getUnboundWildcard());

                               LM.Class capture = withGenerics.interpolateGenerics();

                               LM.Type stringRep = Optional.of(capture.getGenericTypes().get(1))
                                                           .map(LM.Generic.class::cast)
                                                           .map(LM.Generic::getBound)
                                                           .map(LM.Interface.class::cast)
                                                           .map(LM.Interface::getGenericTypes)
                                                           .map(types -> types.get(0))
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
