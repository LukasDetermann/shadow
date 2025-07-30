package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.exports.ExportsRenderable;
import io.determann.shadow.api.dsl.module.*;
import io.determann.shadow.api.dsl.opens.OpensRenderable;
import io.determann.shadow.api.dsl.provides.ProvidesRenderable;
import io.determann.shadow.api.dsl.requires.RequiresRenderable;
import io.determann.shadow.api.dsl.uses.UsesRenderable;
import io.determann.shadow.api.renderer.RenderingContext;

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

   public ModuleDsl() {}

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
   public ModuleAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation)
   {
      return addArrayRenderer(new ModuleDsl(this),
                              annotation,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              moduleDsl -> moduleDsl.annotations::add);
   }

   @Override
   public ModuleRequiresStep name(String name)
   {
      return setType(new ModuleDsl(this), name, (moduleDsl, s) -> moduleDsl.name = s);
   }

   @Override
   public ModuleRequiresStep requires(String... requires)
   {
      return addArrayRenderer(new ModuleDsl(this), requires, moduleDsl -> moduleDsl.requires::add);
   }

   @Override
   public ModuleRequiresStep requires(List<? extends RequiresRenderable> requires)
   {
      return addArrayRenderer(new ModuleDsl(this),
                              requires,
                              (renderingContext, requiresRenderable) -> requiresRenderable.renderDeclaration(renderingContext),
                              moduleDsl -> moduleDsl.requires::add);
   }

   @Override
   public ModuleExportsStep exports(String... exports)
   {
      return addArrayRenderer(new ModuleDsl(this), exports, moduleDsl -> moduleDsl.exports::add);
   }

   @Override
   public ModuleExportsStep exports(List<? extends ExportsRenderable> exports)
   {
      return addArrayRenderer(new ModuleDsl(this),
                              exports,
                              (renderingContext, exportsRenderable) -> exportsRenderable.renderDeclaration(renderingContext),
                              moduleDsl -> moduleDsl.exports::add);
   }

   @Override
   public ModuleOpensStep opens(String... opens)
   {
      return addArrayRenderer(new ModuleDsl(this), opens, moduleDsl -> moduleDsl.opens::add);
   }

   @Override
   public ModuleOpensStep opens(List<? extends OpensRenderable> opens)
   {
      return addArrayRenderer(new ModuleDsl(this),
                              opens,
                              (renderingContext, opensRenderable) -> opensRenderable.renderDeclaration(renderingContext),
                              moduleDsl -> moduleDsl.opens::add);
   }

   @Override
   public ModuleUsesStep uses(String... uses)
   {
      return addArrayRenderer(new ModuleDsl(this), uses, moduleDsl -> moduleDsl.uses::add);
   }

   @Override
   public ModuleUsesStep uses(List<? extends UsesRenderable> uses)
   {
      return addArrayRenderer(new ModuleDsl(this),
                              uses,
                              (renderingContext, usesRenderable) -> usesRenderable.renderDeclaration(renderingContext),
                              moduleDsl -> moduleDsl.uses::add);
   }

   @Override
   public ModuleProvidesStep provides(String... provides)
   {
      return addArrayRenderer(new ModuleDsl(this), provides, moduleDsl -> moduleDsl.provides::add);
   }

   @Override
   public ModuleProvidesStep provides(List<? extends ProvidesRenderable> provides)
   {
      return addArrayRenderer(new ModuleDsl(this),
                              provides,
                              (renderingContext, providesRenderable) -> providesRenderable.renderDeclaration(renderingContext),
                              moduleDsl -> moduleDsl.provides::add);
   }

   @Override
   public String renderModuleInfo(RenderingContext renderingContext)
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
