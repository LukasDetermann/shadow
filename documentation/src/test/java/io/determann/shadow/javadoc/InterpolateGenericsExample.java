package io.determann.shadow.javadoc;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
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
                               Ap.Class myClass = context.getClassOrThrow("MyClass");
                               Ap.Declared string = context.getDeclaredOrThrow("java.lang.String");

                               Ap.Class withGenerics = myClass.withGenerics(string,
                                                                            //the unboundWildcard will be replaced with the result
                                                                            context.getConstants().getUnboundWildcard());

                               Ap.Class capture = withGenerics.interpolateGenerics();

                               Ap.Type stringRep = Optional.of(capture.getGenericUsages().get(1))
                                                           .map(Ap.Generic.class::cast)
                                                           .map(Ap.Generic::getBound)
                                                           .map(Ap.Interface.class::cast)
                                                           .map(Ap.Interface::getGenericUsages)
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
