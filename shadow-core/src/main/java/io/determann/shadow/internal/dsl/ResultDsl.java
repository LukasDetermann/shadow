package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.result.ResultAnnotateStep;
import io.determann.shadow.api.dsl.result.ResultRenderable;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.C_Annotation;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static io.determann.shadow.internal.dsl.DslSupport.*;

public class ResultDsl
      implements ResultAnnotateStep,
                 ResultRenderable
{
   private final List<Function<RenderingContext, String>> annotations = new ArrayList<>();
   private Function<RenderingContext, String> type;

   public ResultDsl()
   {
   }

   private ResultDsl(ResultDsl other)
   {
      this.annotations.addAll(other.annotations);
      this.type = other.type;
   }

   @Override
   public ResultAnnotateStep annotate(String... annotation)
   {
      return addArrayRenderer(new ResultDsl(this), annotation, resultDsl -> resultDsl.annotations::add);
   }

   @Override
   public ResultAnnotateStep annotate(C_Annotation... annotation)
   {
      return addArrayRenderer(new ResultDsl(this),
                              annotation,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                              resultDsl -> resultDsl.annotations::add);

   }

   @Override
   public ResultRenderable type(String type)
   {
      return setTypeRenderer(new ResultDsl(this),
                             type,
                             (resultDsl, function) -> resultDsl.type = function);
   }

   @Override
   public ResultRenderable type(C_Type type)
   {
      return addTypeRenderer(new ResultDsl(this),
                             type,
                             (renderingContext, type1) -> Renderer.render(type1).type(renderingContext),
                             resultDsl -> resultDsl.annotations::add);
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();

      renderElement(sb, annotations, " ", renderingContext, " ");

      sb.append(type.apply(renderingContext));

      return sb.toString();
   }
}
