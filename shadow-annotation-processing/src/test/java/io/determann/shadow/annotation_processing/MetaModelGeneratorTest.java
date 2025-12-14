package io.determann.shadow.annotation_processing;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.dsl.RenderingContext.createRenderingContext;
import static io.determann.shadow.internal.annotation_processing.MetaModelGenerator.createMetaModel;

class MetaModelGeneratorTest
{
   @Test
   void allParams()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Annotation myAnnotation = context.getAnnotationOrThrow("MyAnnotation");

                               String declaration = createMetaModel(myAnnotation).renderDeclaration(createRenderingContext());
                               String withTimeVariance = declaration.replaceFirst(
                                     "date = \"\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+\"\\)",
                                     "date = \"1970-01-01T00:00:00.0000000\")");

                               Assertions.assertEquals(EXPECTED, withTimeVariance);
                            })
                   .withCodeToCompile("MyOtherAnnotation.java",
                                      """
                                      public @interface MyOtherAnnotation {}""")
                   .withCodeToCompile("MyAnnotation.java",
                                      """
                                      public @interface MyAnnotation{
                                         String string();
                                         boolean boolean_();
                                         byte byte_();
                                         short short_();
                                         int int_();
                                         long long_();
                                         char char_();
                                         float float_();
                                         double double_();
                                         Class<?> type();
                                         java.lang.annotation.RetentionPolicy enum_();
                                         MyOtherAnnotation annotation();
                                         int[] values();
                                      }""")
                   .compile();
   }

   private static final String EXPECTED = """
                                          import java.util.Objects;
                                          import java.util.Map;
                                          import io.determann.shadow.api.query.Implementation;
                                          import static io.determann.shadow.api.query.Provider.requestOrThrow;
                                          import static io.determann.shadow.api.query.Operations.*;
                                          import io.determann.shadow.api.annotation_processing.Ap;
                                          import javax.annotation.processing.Generated;
                                          import io.determann.shadow.api.C;
                                          
                                          @Generated(value = "io.determann.shadow.internal.annotation_processing.MetaModelGenerator", date = "1970-01-01T00:00:00.0000000")
                                          public class MyAnnotationMetaModel implements Ap.AnnotationUsage {
                                             private static final String QUALIFIED_ANNOTATION_NAME = "MyAnnotation";
                                             private final C.AnnotationUsage myAnnotation;
                                             MyAnnotationMetaModel(C.AnnotationUsage usage) {
                                                Objects.requireNonNull(usage);
                                                C.Annotation annotation = requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION);
                                                String qualifiedName = requestOrThrow(annotation, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
                                          
                                                if (!QUALIFIED_ANNOTATION_NAME.equals(qualifiedName)) {
                                          
                                                   throw new IllegalArgumentException("This Meta-Model represents a usage of \\"" +
                                                                                      QUALIFIED_ANNOTATION_NAME + "\\" not \\"" + qualifiedName + "\\"");
                                                }
                                                this.myAnnotation = usage;
                                             }
                                          
                                             public Ap.AnnotationValue.StringValue string() {
                                                return (Ap.AnnotationValue.StringValue) requestOrThrow(myAnnotation, ANNOTATION_USAGE_GET_VALUE, "string");
                                             }
                                             public Ap.AnnotationValue.BooleanValue boolean_() {
                                                return (Ap.AnnotationValue.BooleanValue) requestOrThrow(myAnnotation, ANNOTATION_USAGE_GET_VALUE, "boolean_");
                                             }
                                             public Ap.AnnotationValue.ByteValue byte_() {
                                                return (Ap.AnnotationValue.ByteValue) requestOrThrow(myAnnotation, ANNOTATION_USAGE_GET_VALUE, "byte_");
                                             }
                                             public Ap.AnnotationValue.ShortValue short_() {
                                                return (Ap.AnnotationValue.ShortValue) requestOrThrow(myAnnotation, ANNOTATION_USAGE_GET_VALUE, "short_");
                                             }
                                             public Ap.AnnotationValue.IntegerValue int_() {
                                                return (Ap.AnnotationValue.IntegerValue) requestOrThrow(myAnnotation, ANNOTATION_USAGE_GET_VALUE, "int_");
                                             }
                                             public Ap.AnnotationValue.LongValue long_() {
                                                return (Ap.AnnotationValue.LongValue) requestOrThrow(myAnnotation, ANNOTATION_USAGE_GET_VALUE, "long_");
                                             }
                                             public Ap.AnnotationValue.CharacterValue char_() {
                                                return (Ap.AnnotationValue.CharacterValue) requestOrThrow(myAnnotation, ANNOTATION_USAGE_GET_VALUE, "char_");
                                             }
                                             public Ap.AnnotationValue.FloatValue float_() {
                                                return (Ap.AnnotationValue.FloatValue) requestOrThrow(myAnnotation, ANNOTATION_USAGE_GET_VALUE, "float_");
                                             }
                                             public Ap.AnnotationValue.DoubleValue double_() {
                                                return (Ap.AnnotationValue.DoubleValue) requestOrThrow(myAnnotation, ANNOTATION_USAGE_GET_VALUE, "double_");
                                             }
                                             public Ap.AnnotationValue.TypeValue type() {
                                                return (Ap.AnnotationValue.TypeValue) requestOrThrow(myAnnotation, ANNOTATION_USAGE_GET_VALUE, "type");
                                             }
                                             public Ap.AnnotationValue.EnumValue enum_() {
                                                return (Ap.AnnotationValue.EnumValue) requestOrThrow(myAnnotation, ANNOTATION_USAGE_GET_VALUE, "enum_");
                                             }
                                             public Ap.AnnotationValue.AnnotationUsageValue annotation() {
                                                return (Ap.AnnotationValue.AnnotationUsageValue) requestOrThrow(myAnnotation, ANNOTATION_USAGE_GET_VALUE, "annotation");
                                             }
                                             public Ap.AnnotationValue.Values<Ap.AnnotationValue.IntegerValue> values() {
                                                return (Ap.AnnotationValue.Values) requestOrThrow(myAnnotation, ANNOTATION_USAGE_GET_VALUE, "values");
                                             }
                                             @Override
                                             public Map<Ap.Method, Ap.AnnotationValue> getValues() {
                                                return (Map<Ap.Method, Ap.AnnotationValue>) requestOrThrow(myAnnotation, ANNOTATION_USAGE_GET_VALUES);
                                             }
                                             @Override
                                             public Ap.Annotation getAnnotation() {
                                                return (Ap.Annotation) requestOrThrow(myAnnotation, ANNOTATION_USAGE_GET_ANNOTATION);
                                             }
                                             @Override
                                             public Implementation getImplementation() {
                                                return myAnnotation.getImplementation();
                                             }
                                          
                                          }""";
}