package org.determann.shadow.example.processed.test.annotationusage;

@AnnotationUsageAnnotation
public class AnnotationUsageExample
{
   @AnnotationUsageAnnotation(stingValue = "custom Value")
   private String testField;
}
