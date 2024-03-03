package io.determann.shadow.readme;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingAdapter;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.shadow.Shadow;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import java.util.List;

import static io.determann.shadow.api.converter.Converter.convert;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConversionTest
{
   @Test
   void testConversionShadow()
   {
      ProcessorTest.process(context ->
                            {
                               //@formatter:off
//tag::shadow[]
Shadow myField = context.getClassOrThrow("MyClass")
                        .getFieldOrThrow("myField")
                        .getType();
//Converters limit the conversion to possible types
Shadow genericType = convert(myField).toInterfaceOrThrow()
                                     .getGenericTypes()
                                     .get(0);

assertEquals(context.getClassOrThrow("java.lang.String"), genericType);
//end::shadow[]
// @formatter:on
                            })
                   .withCodeToCompile("MyClass.java", """
                         import java.util.List;
                         class MyClass {
                            private List<String> myField;
                         }""")
                   .compile();
   }

   @Test
   void testConversionJdk()
   {
      ProcessorTest.process(context ->
                            {
                               //@formatter:off
//tag::jdk[]
Elements elements = AnnotationProcessingAdapter.getElements(context);
//get a type -> Element data structure
List<? extends Element> myClass = elements.getTypeElement("MyClass")
                                          .getEnclosedElements();

//get fields of that type -> Element data structure
VariableElement myField = ElementFilter
      .fieldsIn(myClass)
      .stream()
      .filter(field -> field.getSimpleName()
                            .toString()
                            .equals("myField"))
      .findAny()
      .orElseThrow();

//get Generic -> switch to Type data structure
TypeMirror genericType = ((DeclaredType) myField.asType()).getTypeArguments().get(0);

//switch back to Element data structure for comparison
Element genericElement = ((DeclaredType) genericType).asElement();

assertEquals(elements.getTypeElement("java.lang.String"), genericElement);
//end::jdk[]
// @formatter:on
                            })
                   .withCodeToCompile("MyClass.java", """
                         import java.util.List;
                         class MyClass {
                            private List<String> myField;
                         }""")
                   .compile();
   }
}