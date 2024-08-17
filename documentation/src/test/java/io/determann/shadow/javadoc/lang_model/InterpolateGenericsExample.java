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
                               ClassLangModel myClass = context.getClassOrThrow("MyClass");
                               DeclaredLangModel string = context.getDeclaredOrThrow("java.lang.String");

                               ClassLangModel withGenerics = context.withGenerics(myClass,
                                                                                  string,
                                                                                  //the unboundWildcard will be replaced with the result
                                                                                  context.getConstants().getUnboundWildcard());

                               ClassLangModel capture = context.interpolateGenerics(withGenerics);

                               ShadowLangModel stringRep = Optional.of(capture.getGenericTypes().get(1))
                                                                   .map(GenericLangModel.class::cast)
                                                                   .map(GenericLangModel::getExtends)
                                                                   .map(InterfaceLangModel.class::cast)
                                                                   .map(InterfaceLangModel::getGenericTypes)
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
