package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.exports.ExportsRenderable;
import io.determann.shadow.api.dsl.module.*;
import io.determann.shadow.api.dsl.opens.OpensRenderable;
import io.determann.shadow.api.dsl.provides.ProvidesRenderable;
import io.determann.shadow.api.dsl.requires.RequiresRenderable;
import io.determann.shadow.api.dsl.uses.UsesRenderable;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.directive.*;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.internal.dsl.DslSupport.*;

public class ModuleDsl
      implements ModuleCopyrightHeaderStep,
                 ModuleRequiresStep
{
   private String copyright;
   private Renderable javadoc;
   private final List<Renderable> annotations = new ArrayList<>();
   private String name;
   private final List<Renderable> requires = new ArrayList<>();
   private final List<Renderable> exports = new ArrayList<>();
   private final List<Renderable> opens = new ArrayList<>();
   private final List<Renderable> uses = new ArrayList<>();
   private final List<Renderable> provides = new ArrayList<>();

   public ModuleDsl()
   {
   }

   private ModuleDsl(ModuleDsl other)
   {
      this.copyright = other.copyright;
      this.javadoc = other.javadoc;
      this.name = other.name;
      this.requires.addAll(other.requires);
      this.exports.addAll(other.exports);
      this.opens.addAll(other.opens);
      this.uses.addAll(other.uses);
      this.provides.addAll(other.provides);
   }

   @Override
   public ModuleJavaDocStep copyright(String copyrightHeader)
   {
      return setType(new ModuleDsl(this),
                     copyrightHeader,
                     (moduleDsl, string) -> moduleDsl.copyright = string);
   }

   @Override
   public ModuleAnnotateStep javadoc(String javadoc)
   {
      return setTypeRenderer(new ModuleDsl(this), javadoc, (moduleDsl, function) -> moduleDsl.javadoc = function);
   }

   @Override
   public ModuleAnnotateStep annotate(String... annotation)
   {
      return addArrayRenderer(new ModuleDsl(this),
                              annotation,
                              (renderingContext, string) -> '@' + string,
                              moduleDsl -> moduleDsl.annotations::add);
   }

   @Override
   public ModuleAnnotateStep annotate(C_AnnotationUsage... annotation)
   {
      return addArrayRenderer(new ModuleDsl(this),
                              annotation,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                              moduleDsl -> moduleDsl.annotations::add);
   }

   @Override
   public ModuleAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return addArray(new ModuleDsl(this),
                      annotation,
                      moduleDsl -> moduleDsl.annotations::add);
   }

   @Override
   public ModuleRequiresStep name(String name)
   {
      return setType(new ModuleDsl(this), name, (moduleDsl, s) -> moduleDsl.name = s);
   }

   @Override
   public ModuleRequiresStep requires(String requires)
   {
      return addTypeRenderer(new ModuleDsl(this), requires, moduleDsl -> moduleDsl.requires::add);
   }

   @Override
   public ModuleRequiresStep requires(C_Requires requires)
   {
      return addTypeRenderer(new ModuleDsl(this),
                             requires,
                             (renderingContext, requires1) -> Renderer.render(requires1).declaration(renderingContext),
                             moduleDsl -> moduleDsl.requires::add);
   }

   @Override
   public ModuleRequiresStep requires(RequiresRenderable requires)
   {
      return addTypeRenderer(new ModuleDsl(this),
                             requires,
                             moduleDsl -> moduleDsl.requires::add);
   }

   @Override
   public ModuleExportsStep exports(String exports)
   {
      return addTypeRenderer(new ModuleDsl(this), exports, moduleDsl -> moduleDsl.exports::add);
   }

   @Override
   public ModuleExportsStep exports(C_Exports exports)
   {
      return addTypeRenderer(new ModuleDsl(this),
                             exports,
                             (renderingContext, exports1) -> Renderer.render(exports1).declaration(renderingContext),
                             moduleDsl -> moduleDsl.exports::add);
   }

   @Override
   public ModuleExportsStep exports(ExportsRenderable exports)
   {
      return addTypeRenderer(new ModuleDsl(this),
                             exports,
                             moduleDsl -> moduleDsl.exports::add);
   }

   @Override
   public ModuleOpensStep opens(String opens)
   {
      return addTypeRenderer(new ModuleDsl(this), opens, moduleDsl -> moduleDsl.opens::add);
   }

   @Override
   public ModuleOpensStep opens(C_Opens opens)
   {
      return addTypeRenderer(new ModuleDsl(this),
                             opens,
                             (renderingContext, opens1) -> Renderer.render(opens1).declaration(renderingContext),
                             moduleDsl -> moduleDsl.opens::add);
   }

   @Override
   public ModuleOpensStep opens(OpensRenderable opens)
   {
      return addTypeRenderer(new ModuleDsl(this),
                             opens,
                             moduleDsl -> moduleDsl.opens::add);
   }

   @Override
   public ModuleUsesStep uses(String uses)
   {
      return addTypeRenderer(new ModuleDsl(this), uses, moduleDsl -> moduleDsl.uses::add);
   }

   @Override
   public ModuleUsesStep uses(C_Uses uses)
   {
      return addTypeRenderer(new ModuleDsl(this),
                             uses,
                             (renderingContext, uses1) -> Renderer.render(uses1).declaration(renderingContext),
                             moduleDsl -> moduleDsl.uses::add);
   }

   @Override
   public ModuleUsesStep uses(UsesRenderable uses)
   {
      return addTypeRenderer(new ModuleDsl(this),
                             uses,
                             moduleDsl -> moduleDsl.uses::add);
   }

   @Override
   public ModuleProvidesStep provides(String provides)
   {
      return addTypeRenderer(new ModuleDsl(this), provides, moduleDsl -> moduleDsl.provides::add);
   }

   @Override
   public ModuleProvidesStep provides(C_Provides provides)
   {
      return addTypeRenderer(new ModuleDsl(this),
                             provides,
                             (renderingContext, provides1) -> Renderer.render(provides1).declaration(renderingContext),
                             moduleDsl -> moduleDsl.provides::add);
   }

   @Override
   public ModuleProvidesStep provides(ProvidesRenderable provides)
   {
      return addTypeRenderer(new ModuleDsl(this),
                             provides,
                             moduleDsl -> moduleDsl.provides::add);
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      if (copyright != null)
      {
         sb.append(copyright)
           .append("\n\n");
      }
      if (javadoc != null)
      {
         sb.append(javadoc.render(renderingContext))
           .append("\n");
      }

      renderElement(sb, annotations, "\n", renderingContext, "\n");

      sb.append("module ")
        .append(name)
        .append(" {\n");

      renderElement(sb, "\n", requires, "\n", renderingContext, "\n");
      renderElement(sb, "\n", exports, "\n", renderingContext, "\n");
      renderElement(sb, "\n", opens, "\n", renderingContext, "\n");
      renderElement(sb, "\n", uses, "\n", renderingContext, "\n");
      renderElement(sb, "\n", provides, "\n", renderingContext, "\n");

      return sb.append('}')
               .toString();
   }
}
