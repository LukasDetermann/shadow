package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageNameStep;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageTypeStep;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageValueStep;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationValue;
import io.determann.shadow.api.shadow.type.C_Annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static io.determann.shadow.internal.dsl.DslSupport.setType;
import static io.determann.shadow.internal.dsl.DslSupport.setTypeRenderer;

public class AnnotationUsageDsl
      implements AnnotationUsageTypeStep,
                 AnnotationUsageNameStep,
                 AnnotationUsageValueStep
{
   private Function<RenderingContext, String> type;
   private final List<Function<RenderingContext, String>> names = new ArrayList<>();
   private final List<Function<RenderingContext, String>> values = new ArrayList<>();

   public AnnotationUsageDsl()
   {
   }

   private AnnotationUsageDsl(AnnotationUsageDsl other)
   {
      this.type = other.type;
      this.names.addAll(other.names);
      this.values.addAll(other.values);
   }

   @Override
   public AnnotationUsageNameStep type(String annotation)
   {
      return setTypeRenderer(new AnnotationUsageDsl(this),
                             annotation,
                             (annotationUsageDsl, renderer) -> annotationUsageDsl.type = renderer);
   }

   @Override
   public AnnotationUsageNameStep type(C_Annotation annotation)
   {
      return setTypeRenderer(new AnnotationUsageDsl(this),
                             annotation,
                             (renderingContext, string) -> Renderer.render(string).type(renderingContext),
                             (annotationUsageDsl, renderer) -> annotationUsageDsl.type = renderer);
   }

   @Override
   public AnnotationUsageValueStep noName()
   {
      return setType(new AnnotationUsageDsl(this),
                     (Function<RenderingContext, String>) renderingContext -> "",
                     (annotationUsageDsl, renderer) -> annotationUsageDsl.names.add(renderer));
   }

   @Override
   public AnnotationUsageValueStep name(String name)
   {
      return setTypeRenderer(new AnnotationUsageDsl(this),
                             name,
                             (renderingContext, string) -> name + " = ",
                             (annotationUsageDsl, renderer) -> annotationUsageDsl.names.add(renderer));
   }

   @Override
   public AnnotationUsageNameStep value(C_AnnotationValue annotationValue)
   {
      return setTypeRenderer(new AnnotationUsageDsl(this),
                             annotationValue,
                             (renderingContext, annotationValue1) -> Renderer.render(annotationValue1).declaration(renderingContext),
                             (annotationUsageDsl, renderer) -> annotationUsageDsl.values.add(renderer));
   }

   @Override
   public AnnotationUsageNameStep value(String annotationValue)
   {
      return setTypeRenderer(new AnnotationUsageDsl(this),
                             annotationValue,
                             (annotationUsageDsl, renderer) -> annotationUsageDsl.values.add(renderer));
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      sb.append(type.apply(renderingContext));

      for (int i = 0; i < names.size(); i++)
      {
         sb.append(names.get(i).apply(renderingContext));
         sb.append(values.get(i).apply(renderingContext));
      }

      return sb.toString();
   }
}
