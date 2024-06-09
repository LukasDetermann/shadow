package io.determann.shadow.javadoc.lang_model;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.converter.TypeConverter;
import io.determann.shadow.api.lang_model.LangModelQueries;
import io.determann.shadow.api.lang_model.query.InterfaceLangModel;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Shadow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;

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

                               Shadow stringRep = Optional.of(query(capture).getGenericTypes().get(1))
                                                          .map(Converter::convert)
                                                          .map(TypeConverter::toGenericOrThrow)
                                                          .map(Generic::getExtends)
                                                          .map(Converter::convert)
                                                          .map(TypeConverter::toInterfaceOrThrow)
                                                          .map(LangModelQueries::query)
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
