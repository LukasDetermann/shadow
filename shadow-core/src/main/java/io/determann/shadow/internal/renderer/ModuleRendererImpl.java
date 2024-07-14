package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.converter.module.DirectiveConverter;
import io.determann.shadow.api.renderer.ModuleRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.module.*;
import io.determann.shadow.api.shadow.structure.Module;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

public class ModuleRendererImpl implements ModuleRenderer
{
   private final RenderingContextWrapper context;
   private final Module module;

   public ModuleRendererImpl(RenderingContext renderingContext, Module module)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.module = module;
   }

   @Override
   public String declaration()
   {
      if (requestOrThrow(module, MODULE_IS_UNNAMED))
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

      if (requestOrThrow(module, MODULE_IS_OPEN))
      {
         sb.append("open ");
      }

      sb.append("module ");
      sb.append(requestOrThrow(module, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));
      sb.append(" {\n");

      Map<DirectiveKind, List<Directive>> kind = requestOrThrow(module, MODULE_GET_DIRECTIVES)
                                                       .stream()
                                                       .collect(groupingBy(Directive::getKind));

      if (kind.containsKey(DirectiveKind.REQUIRES))
      {
         Function<DirectiveConverter, Requires> requiresConverter = DirectiveConverter::toRequiresOrThrow;
         List<Directive> requires = kind.get(DirectiveKind.REQUIRES)
                                        .stream()
                                        .map(Converter::convert)
                                        .map(requiresConverter)
                                        .filter(requires1 -> !requestOrThrow(requires1.getDependency(), QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals("java.base"))
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
                       .map(Converter::convert)
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
      sb.append(requestOrThrow(requires.getDependency(), QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));
      return sb.toString();
   }

   private String render(Exports exports)
   {
      StringBuilder sb = new StringBuilder();

      if (requestOrThrow(exports.getPackage(), PACKAGE_IS_UNNAMED))
      {
         throw new IllegalArgumentException("cant render a unnamed packageName");
      }

      sb.append("exports ");
      sb.append(requestOrThrow(exports.getPackage(), QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));

      if (!exports.toAll())
      {
         sb.append(" to ");
         sb.append(exports.getTargetModules().stream()
                          .map(module1 -> requestOrThrow(module1, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME))
                          .collect(joining(", ")));
      }

      return sb.toString();
   }

   private String render(Opens opens)
   {
      StringBuilder sb = new StringBuilder();

      if (requestOrThrow(opens.getPackage(), PACKAGE_IS_UNNAMED))
      {
         throw new IllegalArgumentException("cant render a unnamed packageName");
      }

      sb.append("opens ");
      sb.append(requestOrThrow(opens.getPackage(), QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));

      if (!opens.toAll())
      {
         sb.append(" to ");
         sb.append(opens.getTargetModules().stream()
                        .map(module1 -> requestOrThrow(module1, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME))
                        .collect(joining(", ")));
      }

      return sb.toString();
   }

   private String render(Uses uses)
   {
      return "uses " + requestOrThrow(uses.getService(), QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
   }

   private String render(Provides provides)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("provides ");
      sb.append(requestOrThrow(provides.getService(), QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));

      if (!provides.getImplementations().isEmpty())
      {
         sb.append(" with ");
         sb.append(provides.getImplementations()
                           .stream()
                           .map(declared -> requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME))
                           .collect(joining(", ")));
      }
      return sb.toString();
   }
}
