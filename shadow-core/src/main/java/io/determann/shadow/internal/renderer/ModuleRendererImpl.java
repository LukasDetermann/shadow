package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.ModuleRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Operations;
import io.determann.shadow.api.shadow.directive.*;
import io.determann.shadow.api.shadow.structure.Module;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrEmpty;
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

      //noinspection OptionalContainsCollection
      Optional<List<AnnotationUsage>> annotationUsages = requestOrEmpty(module, Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES);
      if (!annotationUsages.map(List::isEmpty).orElse(true))
      {
         sb.append(annotationUsages.get()
                         .stream()
                         .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                         .collect(joining()));
      }

      if (requestOrThrow(module, MODULE_IS_OPEN))
      {
         sb.append("open ");
      }

      sb.append("module ");
      sb.append(requestOrThrow(module, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));
      sb.append(" {\n");

      Map<Class<? extends Directive>, List<Directive>> kind = requestOrThrow(module, MODULE_GET_DIRECTIVES)
                                                       .stream()
                                                       .collect(groupingBy(directive -> switch (directive)
                                                       {
                                                          case Exports exports -> Exports.class;
                                                          case Opens opens -> Opens.class;
                                                          case Provides provides -> Provides.class;
                                                          case Requires requires -> Requires.class;
                                                          case Uses uses -> Uses.class;
                                                          default -> throw new IllegalStateException("Unexpected value: " + directive);
                                                       }));

      if (kind.containsKey(Requires.class))
      {
         List<Directive> requires = kind.get(Requires.class)
                                        .stream()
                                        .map(Requires.class::cast)
                                        .filter(requires1 -> !requestOrThrow(requestOrThrow(requires1, REQUIRES_GET_DEPENDENCY),
                                                                             QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals("java.base"))
                                        .collect(Collectors.toList());
         sb.append(render(requires,
                          Requires.class::cast,
                          this::render));
         sb.append('\n');
      }
      if (kind.containsKey(Exports.class))
      {
         sb.append(render(kind.get(Exports.class),
                          Exports.class::cast,
                          this::render));
         sb.append('\n');
      }
      if (kind.containsKey(Opens.class))
      {
         sb.append(render(kind.get(Opens.class),
                          Opens.class::cast,
                          this::render));
         sb.append('\n');
      }
      if (kind.containsKey(Uses.class))
      {
         sb.append(render(kind.get(Uses.class),
                          Uses.class::cast,
                          this::render));
         sb.append('\n');
      }
      if (kind.containsKey(Provides.class))
      {
         sb.append(render(kind.get(Provides.class),
                          Provides.class::cast,
                          this::render));
      }

      sb.append(" }");
      sb.append('\n');

      return sb.toString();
   }

   private <DIRECTIVE> String render(List<Directive> directives,
                                     Function<Directive, DIRECTIVE> convert,
                                     Function<DIRECTIVE, String> renderer)
   {
      return directives.stream()
                       .map(convert)
                       .map(renderer)
                       .map(s -> s + ';')
                       .collect(joining("\n"))
             + '\n';
   }

   private String render(Requires requires)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("requires ");

      if (requestOrThrow(requires, REQUIRES_IS_TRANSITIVE))
      {
         sb.append("transitive ");
      }
      if (requestOrThrow(requires, REQUIRES_IS_STATIC))
      {
         sb.append("static ");
      }
      sb.append(requestOrThrow(requestOrThrow(requires, REQUIRES_GET_DEPENDENCY), QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));
      return sb.toString();
   }

   private String render(Exports exports)
   {
      StringBuilder sb = new StringBuilder();

      if (requestOrThrow(requestOrThrow(exports, EXPORTS_GET_PACKAGE), PACKAGE_IS_UNNAMED))
      {
         throw new IllegalArgumentException("cant render a unnamed packageName");
      }

      sb.append("exports ");
      sb.append(requestOrThrow(requestOrThrow(exports, EXPORTS_GET_PACKAGE), QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));

      if (!requestOrThrow(exports, EXPORTS_TO_ALL))
      {
         sb.append(" to ");
         sb.append(requestOrThrow(exports, EXPORTS_GET_TARGET_MODULES).stream()
                          .map(module1 -> requestOrThrow(module1, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME))
                          .collect(joining(", ")));
      }

      return sb.toString();
   }

   private String render(Opens opens)
   {
      StringBuilder sb = new StringBuilder();

      if (requestOrThrow(requestOrThrow(opens, OPENS_GET_PACKAGE), PACKAGE_IS_UNNAMED))
      {
         throw new IllegalArgumentException("cant render a unnamed packageName");
      }

      sb.append("opens ");
      sb.append(requestOrThrow(requestOrThrow(opens, OPENS_GET_PACKAGE), QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));

      if (!requestOrThrow(opens, OPENS_TO_ALL))
      {
         sb.append(" to ");
         sb.append(requestOrThrow(opens, OPENS_GET_TARGET_MODULES).stream()
                        .map(module1 -> requestOrThrow(module1, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME))
                        .collect(joining(", ")));
      }

      return sb.toString();
   }

   private String render(Uses uses)
   {
      return "uses " + requestOrThrow(requestOrThrow(uses,  USES_GET_SERVICE), QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
   }

   private String render(Provides provides)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("provides ");
      sb.append(requestOrThrow(requestOrThrow(provides, PROVIDES_GET_SERVICE), QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));

      if (!requestOrThrow(provides, PROVIDES_GET_IMPLEMENTATIONS).isEmpty())
      {
         sb.append(" with ");
         sb.append(requestOrThrow(provides, PROVIDES_GET_IMPLEMENTATIONS)
                           .stream()
                           .map(declared -> requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME))
                           .collect(joining(", ")));
      }
      return sb.toString();
   }
}
