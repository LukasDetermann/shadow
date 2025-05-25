package io.determann.shadow.api.renderer;

public interface AnnotationValueRenderer
{
   /// {@snippet :
   ///
   /// @MyAnnotation(name = "myName")//@highlight substring="myName"
   ///}
   String declaration(RenderingContext renderingContext);
}
