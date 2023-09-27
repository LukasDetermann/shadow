package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.QualifiedNameable;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.converter.module.DirectiveConverter;
import io.determann.shadow.api.renderer.ModuleRenderer;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.module.*;
import io.determann.shadow.impl.ShadowApiImpl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

public class ModuleRendererImpl implements ModuleRenderer
{
   private final Context context;
   private final Module module;

   public ModuleRendererImpl(Module module)
   {
      this.context = ((ShadowApiImpl) module.getApi()).getRenderingContext();
      this.module = module;
   }

   @Override
   public String declaration()
   {
      if (module.isUnnamed())
      {
         throw new IllegalArgumentException("cant render a unnamed module");
      }

      StringBuilder sb = new StringBuilder();

      if (!module.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(module.getDirectAnnotationUsages()
                         .stream()
                         .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                         .collect(Collectors.joining()));
      }

      if (module.isOpen())
      {
         sb.append("open ");
      }

      sb.append("module ");
      sb.append(module.getQualifiedName());
      sb.append(" {\n");

      Map<DirectiveKind, List<Directive>> kind = module.getDirectives()
                                                       .stream()
                                                       .collect(groupingBy(Directive::getKind));

      if (kind.containsKey(DirectiveKind.REQUIRES))
      {
         Function<DirectiveConverter, Requires> requiresConverter = DirectiveConverter::toRequiresOrThrow;
         List<Directive> requires = kind.get(DirectiveKind.REQUIRES)
                                        .stream()
                                        .map(ShadowApi::convert)
                                        .map(requiresConverter)
                                        .filter(requires1 -> !requires1.getDependency().getQualifiedName().equals("java.base"))
                                        .collect(Collectors.toList());
         sb.append(render(requires,
                          requiresConverter,
                          this::render));
         sb.append('\n');
      }
      if (kind.containsKey(DirectiveKind.EXPORTS))
      {
         sb.append(render(kind.get(DirectiveKind.EXPORTS),
                          DirectiveConverter::toExportsOrThrow,
                          this::render));
         sb.append('\n');
      }
      if (kind.containsKey(DirectiveKind.OPENS))
      {
         sb.append(render(kind.get(DirectiveKind.OPENS),
                          DirectiveConverter::toOpensOrThrow,
                          this::render));
         sb.append('\n');
      }
      if (kind.containsKey(DirectiveKind.USES))
      {
         sb.append(render(kind.get(DirectiveKind.USES),
                          DirectiveConverter::toUsesOrThrow,
                          this::render));
         sb.append('\n');
      }
      if (kind.containsKey(DirectiveKind.PROVIDES))
      {
         sb.append(render(kind.get(DirectiveKind.PROVIDES),
                          DirectiveConverter::toProvidesOrThrow,
                          this::render));
      }

      sb.append(" }");
      sb.append('\n');

      return sb.toString();
   }

   private <DIRECTIVE> String render(List<Directive> directives,
                                     Function<DirectiveConverter, DIRECTIVE> converter,
                                     Function<DIRECTIVE, String> renderer)
   {
      return directives.stream()
                       .map(ShadowApi::convert)
                       .map(converter)
                       .map(renderer)
                       .map(s -> s + ';')
                       .collect(joining("\n"))
             + '\n';
   }

   private String render(Requires requires)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("requires ");

      if (requires.isTransitive())
      {
         sb.append("transitive ");
      }
      if (requires.isStatic())
      {
         sb.append("static ");
      }
      sb.append(requires.getDependency().getQualifiedName());
      return sb.toString();
   }

   private String render(Exports exports)
   {
      StringBuilder sb = new StringBuilder();

      if (exports.getPackage().isUnnamed())
      {
         throw new IllegalArgumentException("cant render a unnamed packageName");
      }

      sb.append("exports ");
      sb.append(exports.getPackage().getQualifiedName());

      if (!exports.toAll())
      {
         sb.append(" to ");
         sb.append(exports.getTargetModules().stream()
                          .map(QualifiedNameable::getQualifiedName)
                          .collect(joining(", ")));
      }

      return sb.toString();
   }

   private String render(Opens opens)
   {
      StringBuilder sb = new StringBuilder();

      if (opens.getPackage().isUnnamed())
      {
         throw new IllegalArgumentException("cant render a unnamed packageName");
      }

      sb.append("opens ");
      sb.append(opens.getPackage().getQualifiedName());

      if (!opens.toAll())
      {
         sb.append(" to ");
         sb.append(opens.getTargetModules().stream()
                        .map(QualifiedNameable::getQualifiedName)
                        .collect(joining(", ")));
      }

      return sb.toString();
   }

   private String render(Uses uses)
   {
      return "uses " + uses.getService().getQualifiedName();
   }

   private String render(Provides provides)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("provides ");
      sb.append(provides.getService().getQualifiedName());

      if (!provides.getImplementations().isEmpty())
      {
         sb.append(" with ");
         sb.append(provides.getImplementations().stream().map(QualifiedNameable::getQualifiedName).collect(joining(", ")));
      }
      return sb.toString();
   }
}
