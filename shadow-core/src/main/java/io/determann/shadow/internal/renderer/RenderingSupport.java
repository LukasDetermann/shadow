package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.shadow.C_Annotationable;

import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES;
import static io.determann.shadow.api.Provider.requestOrEmpty;

public class RenderingSupport
{
   static String annotations(RenderingContextWrapper context, C_Annotationable annotationable, char separator)
   {
      return requestOrEmpty(annotationable, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES)
            .map(annotationUsages ->
                       annotationUsages.stream()
                                       .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + separator)
                                       .collect(Collectors.joining()))
            .orElse("");
   }
}
