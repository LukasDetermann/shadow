package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.structure.C_Method;

import java.util.Optional;
import java.util.function.Function;

public interface AnnotationUsageRenderer
{
   /**
    * {@code @MyAnnotation(name = "myName")}
    */
   String usage(RenderingContext renderingContext);

   /**
    * {@code @MyAnnotation(name = "myName")}
    * <p>
    * if an empty optional is supplied the value of the original AnnotationUsage will be used
    */
   String usage(RenderingContext renderingContext, Function<C_Method, Optional<String>> valueRenderer);
}
