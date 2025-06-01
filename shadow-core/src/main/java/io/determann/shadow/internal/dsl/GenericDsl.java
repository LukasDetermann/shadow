package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.class_.ClassRenderable;
import io.determann.shadow.api.dsl.generic.GenericAndExtendsStep;
import io.determann.shadow.api.dsl.generic.GenericAnnotateStep;
import io.determann.shadow.api.dsl.generic.GenericExtendsStep;
import io.determann.shadow.api.dsl.generic.GenericRenderable;
import io.determann.shadow.api.dsl.interface_.InterfaceRenderable;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.internal.renderer.RenderingContextWrapper;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.internal.dsl.DslSupport.*;
import static io.determann.shadow.internal.renderer.RenderingContextWrapper.wrap;

public class GenericDsl
      implements GenericAnnotateStep,
                 GenericExtendsStep,
                 GenericAndExtendsStep
{
   private final List<Renderable> annotations = new ArrayList<>();
   private String name;
   private final List<Renderable> extends_ = new ArrayList<>();

   public GenericDsl()
   {
   }

   private GenericDsl(GenericDsl other)
   {
      this.annotations.addAll(other.annotations);
      this.name = other.name;
      this.extends_.addAll(other.extends_);
   }

   @Override
   public GenericAnnotateStep annotate(String... annotation)
   {
      return addArrayRenderer(new GenericDsl(this),
                              annotation,
                              (renderingContext, string) -> '@' + string,
                              genericDsl -> genericDsl.annotations::add);
   }

   @Override
   public GenericAnnotateStep annotate(C_AnnotationUsage... annotation)
   {
      return addArrayRenderer(new GenericDsl(this),
                              annotation,
                              (context, cAnnotation) -> Renderer.render(cAnnotation).declaration(context),
                              genericDsl -> genericDsl.annotations::add);
   }

   @Override
   public GenericAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return addArray(new GenericDsl(this),
                      annotation,
                      genericDsl -> genericDsl.annotations::add);
   }

   @Override
   public GenericExtendsStep name(String name)
   {
      return setType(new GenericDsl(this),
                     name,
                     (genericDsl, string) -> genericDsl.name = string);
   }

   @Override
   public GenericAndExtendsStep extends_(String bound)
   {
      return addTypeRenderer(new GenericDsl(this),
                             bound,
                             genericDsl -> genericDsl.extends_::add);
   }

   @Override
   public GenericAndExtendsStep extends_(C_Class bound)
   {
      return addTypeRenderer(new GenericDsl(this),
                             bound,
                             (renderingContext, cClass) -> Renderer.render(cClass).type(renderingContext),
                             genericDsl -> genericDsl.extends_::add);
   }

   @Override
   public GenericAndExtendsStep extends_(ClassRenderable bound)
   {
      return addTypeRenderer(new GenericDsl(this),
                             bound,
                             genericDsl -> genericDsl.extends_::add);
   }

   @Override
   public GenericAndExtendsStep extends_(C_Interface bound)
   {
      return addTypeRenderer(new GenericDsl(this),
                             bound,
                             (renderingContext, cInterface) -> Renderer.render(cInterface).type(renderingContext),
                             genericDsl -> genericDsl.extends_::add);
   }

   @Override
   public GenericAndExtendsStep extends_(InterfaceRenderable bound)
   {
      return addTypeRenderer(new GenericDsl(this),
                             bound,
                             genericDsl -> genericDsl.extends_::add);
   }

   @Override
   public GenericAndExtendsStep extends_(C_Generic bound)
   {
      return addTypeRenderer(new GenericDsl(this),
                             bound,
                             (renderingContext, cGeneric) -> Renderer.render(cGeneric).type(renderingContext),
                             genericDsl -> genericDsl.extends_::add);
   }

   @Override
   public GenericAndExtendsStep extends_(GenericRenderable bound)
   {
      return addTypeRenderer(new GenericDsl(this),
                             bound,
                             genericDsl -> genericDsl.extends_::add);
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      if (renderingContext instanceof RenderingContextWrapper wrapper && wrapper.isGenericUsage())
      {
         return name;
      }

      StringBuilder sb = new StringBuilder();

      renderElement(sb, annotations, " ", renderingContext, " ");

      sb.append(name);

      if (!(renderingContext instanceof RenderingContextWrapper wrapper) || !wrapper.isGenericUsage())
      {
         RenderingContextWrapper wrapped = wrap(renderingContext);
         wrapped.setGenericUsage(true);
         renderElement(sb, " extends ", extends_, wrapped, " & ");
      }

      return sb.toString();
   }
}
