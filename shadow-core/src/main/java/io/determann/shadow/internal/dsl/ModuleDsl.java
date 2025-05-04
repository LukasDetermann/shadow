package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.module.*;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.directive.*;
import io.determann.shadow.api.shadow.type.C_Annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static io.determann.shadow.internal.dsl.DslSupport.*;
import static java.util.stream.Collectors.joining;

public class ModuleDsl
      implements ModuleJavaDocStep,
                 ModuleRequiresStep
{
   private Function<RenderingContext, String> javadoc;
   private final List<Function<RenderingContext, String>> annotations = new ArrayList<>();
   private String name;
   private final List<Function<RenderingContext, String>> requires = new ArrayList<>();
   private final List<Function<RenderingContext, String>> exports = new ArrayList<>();
   private final List<Function<RenderingContext, String>> opens = new ArrayList<>();
   private final List<Function<RenderingContext, String>> uses = new ArrayList<>();
   private final List<Function<RenderingContext, String>> provides = new ArrayList<>();

   public ModuleDsl()
   {
   }

   private ModuleDsl(ModuleDsl other)
   {
      this.javadoc = other.javadoc;
      this.name = other.name;
      this.requires.addAll(other.requires);
      this.exports.addAll(other.exports);
      this.opens.addAll(other.opens);
      this.uses.addAll(other.uses);
      this.provides.addAll(other.provides);
   }

   @Override
   public ModuleAnnotateStep javadoc(String javadoc)
   {
      return set(new ModuleDsl(this),
                 (moduleDsl, function) -> moduleDsl.javadoc = function,
                 javadoc);
   }

   @Override
   public ModuleAnnotateStep annotate(String... annotation)
   {
      return add(new ModuleDsl(this), moduleDsl -> moduleDsl.annotations::add, annotation);
   }

   @Override
   public ModuleAnnotateStep annotate(C_Annotation... annotation)
   {
      return add(new ModuleDsl(this),
                 moduleDsl -> moduleDsl.annotations::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 annotation);
   }

   @Override
   public ModuleRequiresStep name(String name)
   {
      return setString(new ModuleDsl(this),
                       (moduleDsl, s) -> moduleDsl.name = s,
                       name);
   }

   @Override
   public ModuleRequiresStep requires(String requires)
   {
      return add(new ModuleDsl(this),
                 moduleDsl -> moduleDsl.requires::add,
                 requires);
   }

   @Override
   public ModuleRequiresStep requires(C_Requires requires)
   {
      return add(new ModuleDsl(this),
                 moduleDsl -> moduleDsl.requires::add,
                 (renderingContext, requires1) -> Renderer.render(requires1).declaration(renderingContext),
                 requires);
   }

   @Override
   public ModuleExportsStep exports(String exports)
   {
      return add(new ModuleDsl(this),
                 moduleDsl -> moduleDsl.exports::add,
                 exports);
   }

   @Override
   public ModuleExportsStep exports(C_Exports exports)
   {
      return add(new ModuleDsl(this),
                 moduleDsl -> moduleDsl.exports::add,
                 (renderingContext, exports1) -> Renderer.render(exports1).declaration(renderingContext),
                 exports);
   }

   @Override
   public ModuleOpensStep opens(String opens)
   {
      return add(new ModuleDsl(this),
                 moduleDsl -> moduleDsl.opens::add,
                 opens);
   }

   @Override
   public ModuleOpensStep opens(C_Opens opens)
   {
      return add(new ModuleDsl(this),
                 moduleDsl -> moduleDsl.opens::add,
                 (renderingContext, opens1) -> Renderer.render(opens1).declaration(renderingContext),
                 opens);
   }

   @Override
   public ModuleUsesStep uses(String uses)
   {
      return add(new ModuleDsl(this),
                 moduleDsl -> moduleDsl.uses::add,
                 uses);
   }

   @Override
   public ModuleUsesStep uses(C_Uses uses)
   {
      return add(new ModuleDsl(this),
                 moduleDsl -> moduleDsl.uses::add,
                 (renderingContext, uses1) -> Renderer.render(uses1).declaration(renderingContext),
                 uses);
   }

   @Override
   public ModuleProvidesStep provides(String provides)
   {
      return add(new ModuleDsl(this),
                 moduleDsl -> moduleDsl.provides::add,
                 provides);
   }

   @Override
   public ModuleProvidesStep provides(C_Provides provides)
   {
      return add(new ModuleDsl(this),
                 moduleDsl -> moduleDsl.provides::add,
                 (renderingContext, provides1) -> Renderer.render(provides1).declaration(renderingContext),
                 provides);
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      if (javadoc != null)
      {
         sb.append(javadoc.apply(renderingContext));
         sb.append("\n");
      }
      if (!annotations.isEmpty())
      {
         sb.append(this.annotations.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining("\n")));
         sb.append('\n');
      }
      sb.append("module ");
      sb.append(name);
      sb.append(" {\n");

      sb.append(requires.stream().map(renderer -> renderer.apply(renderingContext) + "\n").collect(joining()));
      sb.append(exports.stream().map(renderer -> renderer.apply(renderingContext) + "\n").collect(joining()));
      sb.append(opens.stream().map(renderer -> renderer.apply(renderingContext) + "\n").collect(joining()));
      sb.append(uses.stream().map(renderer -> renderer.apply(renderingContext) + "\n").collect(joining()));
      sb.append(provides.stream().map(renderer -> renderer.apply(renderingContext) + "\n").collect(joining()));

      sb.append('}');

      return sb.toString();
   }
}
