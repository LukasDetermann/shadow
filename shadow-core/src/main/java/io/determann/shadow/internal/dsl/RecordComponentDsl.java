package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.record_component.RecordComponentAnnotateStep;
import io.determann.shadow.api.dsl.record_component.RecordComponentRenderable;
import io.determann.shadow.api.dsl.record_component.RecordComponentTypeStep;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.C_Annotation;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static io.determann.shadow.internal.dsl.DslSupport.*;

public class RecordComponentDsl
      implements RecordComponentAnnotateStep,
                 RecordComponentTypeStep,
                 RecordComponentRenderable
{
   private final List<Function<RenderingContext, String>> annotations = new ArrayList<>();
   private String name;
   private Function<RenderingContext, String> type;

   public RecordComponentDsl() {}

   private RecordComponentDsl(RecordComponentDsl other)
   {
      this.annotations.addAll(other.annotations);
      this.name = other.name;
      this.type = other.type;
   }

   @Override
   public RecordComponentAnnotateStep annotate(String... annotation)
   {
      return addArrayRenderer(new RecordComponentDsl(this), annotation, recordComponentDsl -> recordComponentDsl.annotations::add);
   }

   @Override
   public RecordComponentAnnotateStep annotate(C_Annotation... annotation)
   {
      return addArrayRenderer(new RecordComponentDsl(this),
                              annotation,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                              recordComponentDsl -> recordComponentDsl.annotations::add);
   }

   @Override
   public RecordComponentTypeStep name(String name)
   {
      return setType(new RecordComponentDsl(this), name, (recordComponentDsl, s) -> recordComponentDsl.name = s);
   }

   @Override
   public RecordComponentRenderable type(C_Type type)
   {
      return setTypeRenderer(new RecordComponentDsl(this),
                             type,
                             (renderingContext, type1) -> Renderer.render(type1).type(renderingContext),
                             (recordComponentDsl, function) -> recordComponentDsl.type = function);
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();

      renderElement(sb, annotations, " ", renderingContext, " ");

      sb.append(type.apply(renderingContext));
      sb.append(" ");
      sb.append(name);

      return sb.toString();
   }
}
