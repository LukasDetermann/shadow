package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.TypeRenderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.record_component.RecordComponentAnnotateStep;
import io.determann.shadow.api.dsl.record_component.RecordComponentNameStep;
import io.determann.shadow.api.dsl.record_component.RecordComponentRenderable;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.internal.dsl.DslSupport.*;

public class RecordComponentDsl
      implements RecordComponentAnnotateStep,
                 RecordComponentNameStep,
                 RecordComponentRenderable
{
   private final List<Renderable> annotations = new ArrayList<>();
   private String name;
   private Renderable type;

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
      return addArrayRenderer(new RecordComponentDsl(this),
                              annotation,
                              (context, string) -> '@' + context.renderName(string),
                              recordComponentDsl -> recordComponentDsl.annotations::add);
   }

   @Override
   public RecordComponentAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation)
   {
      return addArrayRenderer(new RecordComponentDsl(this),
                              annotation,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              recordComponentDsl -> recordComponentDsl.annotations::add);
   }

   @Override
   public RecordComponentRenderable name(String name)
   {
      return setType(new RecordComponentDsl(this), name, (recordComponentDsl, s) -> recordComponentDsl.name = s);
   }

   @Override
   public RecordComponentNameStep type(String type)
   {
      return setType(new RecordComponentDsl(this),
                     type,
                     (recordComponentDsl, string) -> recordComponentDsl.type = context -> context.renderName(string));
   }

   @Override
   public RecordComponentNameStep type(TypeRenderable type)
   {
      return setTypeRenderer(new RecordComponentDsl(this),
                             type,
                             (renderingContext, typeRenderable) -> typeRenderable.renderName(renderingContext),
                             (recordComponentDsl, function) -> recordComponentDsl.type = function);
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();

      renderElement(sb, annotations, " ", renderingContext, " ");

      sb.append(type.render(renderingContext));
      sb.append(" ");
      sb.append(name);

      return sb.toString();
   }
}
