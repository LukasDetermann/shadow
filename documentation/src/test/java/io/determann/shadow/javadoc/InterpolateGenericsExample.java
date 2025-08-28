package io.determann.shadow.javadoc;

import io.determann.shadow.api.annotation_processing.AP;
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
                               AP.Class myClass = context.getClassOrThrow("MyClass");
                               AP.Declared string = context.getDeclaredOrThrow("java.lang.String");

                               AP.Class withGenerics = myClass.withGenerics(string,
                                                                            //the unboundWildcard will be replaced with the result
                                                                            context.getConstants().getUnboundWildcard());

                               AP.Class capture = withGenerics.interpolateGenerics();

                               AP.Type stringRep = Optional.of(capture.getGenericTypes().get(1))
                                                           .map(AP.Generic.class::cast)
                                                           .map(AP.Generic::getBound)
                                                           .map(AP.Interface.class::cast)
                                                           .map(AP.Interface::getGenericTypes)
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
