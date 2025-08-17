package io.determann.shadow.api.dsl.enum_constant;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface EnumConstantAnnotateStep
      extends EnumConstantNameStep
{
   @Contract(value = "_ -> new", pure = true)
   EnumConstantAnnotateStep annotate(String... annotation);

   @Contract(value = "_ -> new", pure = true)
   default EnumConstantAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   @Contract(value = "_ -> new", pure = true)
   EnumConstantAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
