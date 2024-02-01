package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.Method;

import java.util.Optional;
import java.util.function.Function;

public interface AnnotationUsageRenderer
{
   /**
    * {@code @MyAnnotation(name = "myName")}
    */
   String usage();

   /**
    * {@code @MyAnnotation(name = "myName")}
    * <p>
    * if an empty optional is supplied the value of the original AnnotationUsage will be used
    */
   String usage(Function<Method, Optional<String>> valueRenderer);
}
