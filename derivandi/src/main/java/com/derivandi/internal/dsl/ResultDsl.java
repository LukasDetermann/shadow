package com.derivandi.internal.dsl;


import com.derivandi.api.dsl.RenderingContext;
import com.derivandi.api.dsl.TypeRenderable;
import com.derivandi.api.dsl.annotation_usage.AnnotationUsageRenderable;
import com.derivandi.api.dsl.result.ResultAnnotateStep;
import com.derivandi.api.dsl.result.ResultRenderable;

import java.util.ArrayList;
import java.util.List;

import static com.derivandi.internal.dsl.DslSupport.*;

public class ResultDsl
      implements ResultAnnotateStep,
                 ResultRenderable
{
   private final List<Renderable> annotations = new ArrayList<>();
   private Renderable type;

   public ResultDsl() {}

   private ResultDsl(ResultDsl other)
   {
      this.annotations.addAll(other.annotations);
      this.type = other.type;
   }

   @Override
   public ResultAnnotateStep annotate(String... annotation)
   {
      return addArrayRenderer(new ResultDsl(this),
                              annotation,
                              (renderingContext, string) -> '@' + string,
                              resultDsl -> resultDsl.annotations::add);
   }

   @Override
   public ResultAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation)
   {
      return addArrayRenderer(new ResultDsl(this),
                              annotation,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              resultDsl -> resultDsl.annotations::add);
   }

   @Override
   public ResultRenderable type(String type)
   {
      return setType(new ResultDsl(this),
                     type,
                     (resultDsl, string) -> resultDsl.type = context -> context.renderName(string));
   }

   @Override
   public ResultRenderable type(TypeRenderable type)
   {
      return setType(new ResultDsl(this),
                     type,
                     (resultDsl, typeNameRenderable) -> resultDsl.type = typeNameRenderable::renderType);
   }

   @Override
   public ResultRenderable surroundingType()
   {
      return setType(new ResultDsl(this),
                     (Renderable) renderingContext -> renderingContext
                           .getSurrounding()
                           .stream()
                           .filter(TypeRenderable.class::isInstance)
                           .map(TypeRenderable.class::cast)
                           .map(typeRenderable -> typeRenderable.renderType(renderingContext))
                           .findFirst()
                           .orElseThrow(() -> new IllegalStateException("Result needs to be contained in a TypeRenderable")),
                     (resultDsl, renderable) -> resultDsl.type = renderable);
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();

      renderElement(sb, annotations, " ", renderingContext, " ");

      sb.append(type.render(renderingContext));

      return sb.toString();
   }
}