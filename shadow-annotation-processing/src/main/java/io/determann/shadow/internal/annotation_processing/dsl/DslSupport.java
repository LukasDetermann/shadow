package io.determann.shadow.internal.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import io.determann.shadow.api.annotation_processing.dsl.package_.PackageRenderable;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

/// implementation note: a bit overkill, but minimises bug potential
@NotNullByDefault
interface DslSupport
{
   static <INSTANCE> INSTANCE addTypeRenderer(INSTANCE instance,
                                              String string,
                                              Function<INSTANCE, Consumer<Renderable>> toAddToSupplier)
   {
      return addTypeRenderer(instance, renderingContext -> string, toAddToSupplier);
   }

   static <INSTANCE, FROM> INSTANCE addTypeRenderer(INSTANCE instance,
                                                    FROM type,
                                                    BiFunction<RenderingContext, FROM, String> renderer,
                                                    Function<INSTANCE, Consumer<Renderable>> toAddToSupplier)
   {
      return addTypeRenderer(instance, renderingContext -> renderer.apply(renderingContext, type), toAddToSupplier);
   }

   static <INSTANCE> INSTANCE addTypeRenderer(INSTANCE instance,
                                              Renderable renderable,
                                              Function<INSTANCE, Consumer<Renderable>> toAddToSupplier)
   {
      requireNonNull(renderable);

      Consumer<Renderable> renderingConsumer = toAddToSupplier.apply(instance);

      renderingConsumer.accept(renderable);

      return instance;
   }

   static <INSTANCE> INSTANCE addArrayRenderer(INSTANCE instance,
                                               String[] strings,
                                               Function<INSTANCE, Consumer<Renderable>> toAddToSupplier)
   {
      return addArrayRenderer(instance, strings, (renderingContext, s) -> s, toAddToSupplier);
   }

   static <INSTANCE, TYPE> INSTANCE addArray2(INSTANCE instance,
                                              TYPE[] types,
                                              BiConsumer<INSTANCE, TYPE> toAddToSupplier)
   {
      requireNonNull(instance);

      for (TYPE type : types)
      {
         requireNonNull(type);
         toAddToSupplier.accept(instance, type);
      }
      return instance;
   }

   static <INSTANCE, TYPE> INSTANCE addArray2(INSTANCE instance,
                                              Collection<TYPE> types,
                                              BiConsumer<INSTANCE, TYPE> toAddToSupplier)
   {
      requireNonNull(instance);

      for (TYPE type : types)
      {
         requireNonNull(type);
         toAddToSupplier.accept(instance, type);
      }
      return instance;
   }

   static <INSTANCE, FROM> INSTANCE addArrayRenderer(INSTANCE instance,
                                                     List<FROM> types,
                                                     BiFunction<RenderingContext, FROM, String> renderer,
                                                     Function<INSTANCE, Consumer<Renderable>> toAddToSupplier)
   {
      requireNonNull(types);

      Consumer<Renderable> renderingConsumer = toAddToSupplier.apply(instance);

      for (FROM type : types)
      {
         requireNonNull(type);
         renderingConsumer.accept(renderingContext -> renderer.apply(renderingContext, type));
      }

      return instance;
   }

   static <INSTANCE, FROM> INSTANCE addArrayRenderer(INSTANCE instance,
                                                     FROM[] types,
                                                     BiFunction<RenderingContext, FROM, String> renderer,
                                                     Function<INSTANCE, Consumer<Renderable>> toAddToSupplier)
   {
      requireNonNull(types);

      Consumer<Renderable> renderingConsumer = toAddToSupplier.apply(instance);

      for (FROM type : types)
      {
         requireNonNull(type);
         renderingConsumer.accept(renderingContext -> renderer.apply(renderingContext, type));
      }

      return instance;
   }


   static <INSTANCE> INSTANCE setTypeRenderer(INSTANCE instance,
                                              String string,
                                              BiConsumer<INSTANCE, Renderable> toAddToSupplier)
   {
      return setTypeRenderer(instance, string, (renderingContext, o) -> o, toAddToSupplier);
   }

   static <INSTANCE, FROM> INSTANCE setTypeRenderer(INSTANCE instance,
                                                    FROM type,
                                                    BiFunction<RenderingContext, FROM, String> renderer,
                                                    BiConsumer<INSTANCE, Renderable> toAddToSupplier)
   {
      requireNonNull(type);

      toAddToSupplier.accept(instance, renderingContext -> renderer.apply(renderingContext, type));

      return instance;
   }

   static <INSTANCE, TYPE> INSTANCE setType(INSTANCE instance,
                                            TYPE type,
                                            BiConsumer<INSTANCE, TYPE> toAddToSupplier)
   {
      requireNonNull(type);

      toAddToSupplier.accept(instance, type);

      return instance;
   }

   static void renderElement(StringBuilder sb,
                             List<Renderable> renderers,
                             String afterAll,
                             RenderingContext renderingContext,
                             String delimiter)
   {
      if (renderers.isEmpty())
      {
         return;
      }

      renderElement(sb, renderers, renderingContext, delimiter, new Padding(null, null, null, afterAll));
   }

   static void renderElement(StringBuilder sb,
                             List<Renderable> renderers,
                             RenderingContext renderingContext,
                             String delimiter)
   {
      if (renderers.isEmpty())
      {
         return;
      }
      renderElement(sb, renderers, renderingContext, delimiter, new Padding(null, null, null, null));
   }

   static void renderElement(StringBuilder sb,
                             String beforeAll,
                             List<Renderable> renderers,
                             RenderingContext renderingContext,
                             String delimiter)
   {
      if (renderers.isEmpty())
      {
         return;
      }
      renderElement(sb, renderers, renderingContext, delimiter, new Padding(beforeAll, null, null, null));
   }

   static void renderElement(StringBuilder sb,
                             String beforeAll,
                             List<Renderable> renderers,
                             String afterAll,
                             RenderingContext renderingContext,
                             String delimiter)
   {
      if (renderers.isEmpty())
      {
         return;
      }
      renderElement(sb, renderers, renderingContext, delimiter, new Padding(beforeAll, null, null, afterAll));
   }

   static void renderElement(StringBuilder sb,
                             List<Renderable> renderers,
                             RenderingContext renderingContext,
                             String delimiter,
                             Padding padding)
   {
      if (!renderers.isEmpty())
      {
         if (padding.beforeAll() != null)
         {
            sb.append(padding.beforeAll());
         }
         sb.append(renderers.stream().map(renderer ->
                                          {
                                             if (padding.beforeEach() == null && padding.afterEach() == null)
                                             {
                                                return renderer.render(renderingContext);
                                             }
                                             if (padding.beforeEach() != null && padding.afterEach() != null)
                                             {
                                                return padding.beforeEach() + renderer.render(renderingContext) + padding.afterEach();
                                             }
                                             if (padding.beforeEach() != null)
                                             {
                                                return padding.beforeEach() + renderer.render(renderingContext);
                                             }
                                             return renderer.render(renderingContext) + padding.afterEach();
                                          }).collect(joining(delimiter)));
         if (padding.afterAll() != null)
         {
            sb.append(padding.afterAll());
         }
      }
   }

   static String indent(RenderingContext context, String s)
   {
      requireNonNull(context);
      requireNonNull(s);

      return s.lines().map(s1 ->
                           {
                              if (s1.isBlank())
                              {
                                 return s1;
                              }
                              return context.getLineIndentation() + s1;
                           }).collect(joining("\n"));
   }

   static Optional<String> renderPackageName(@Nullable PackageRenderable packageRenderable, RenderingContext context)
   {
      if (packageRenderable == null)
      {
         return Optional.empty();
      }
      String packageName = packageRenderable.renderQualifiedName(context);
      if (packageName.isEmpty())
      {
         return Optional.empty();
      }
      return Optional.of(packageName);
   }

   static Optional<String> renderPackageDeclaration(PackageRenderable packageRenderable, RenderingContext context)
   {
      return renderPackageName(packageRenderable, context)
            .map(s -> "package " + s + ';');
   }
}
