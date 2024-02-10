package io.determann.shadow.javadoc.lang_model;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.converter.ShadowConverter;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.*;
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
                               Class myClass = context.getClassOrThrow("MyClass");
                               Declared string = context.getDeclaredOrThrow("java.lang.String");

                               Class withGenerics = context.withGenerics(myClass,
                                                                         string,
                                                                         //the unboundWildcard will be replaced with the result
                                                                         context.getConstants().getUnboundWildcard());

                               Class capture = context.interpolateGenerics(withGenerics);

                               Shadow stringRep = Optional.of(capture.getGenericTypes().get(1))
                                                          .map(Converter::convert)
                                                          .map(ShadowConverter::toGenericOrThrow)
                                                          .map(Generic::getExtends)
                                                          .map(Converter::convert)
                                                          .map(ShadowConverter::toInterfaceOrThrow)
                                                          .map(Interface::getGenericTypes)
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
