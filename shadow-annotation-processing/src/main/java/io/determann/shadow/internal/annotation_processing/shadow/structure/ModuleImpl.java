package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.NoType;
import java.util.*;
import java.util.function.Supplier;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static io.determann.shadow.api.annotation_processing.dsl.Dsl.moduleInfo;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;
import static java.util.stream.Collector.of;

public class ModuleImpl implements Ap.Module
{
   private final ModuleElement moduleElement;
   private final NoType noType;
   private final Ap.Context context;


   public ModuleImpl(Ap.Context context, ModuleElement moduleElement)
   {
      this.context = context;
      noType = (NoType) moduleElement.asType();
      this.moduleElement = moduleElement;
   }

   public ModuleElement getElement()
   {
      return moduleElement;
   }

   @Override
   public List<Ap.Package> getPackages()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .map(PackageElement.class::cast)
                         .map(element -> adapt(getApi(), element))
                         .toList();
   }

   @Override
   public String getQualifiedName()
   {
      return getElement().getQualifiedName().toString();
   }

   @Override
   public List<Ap.Declared> getDeclared()
   {
      return getPackages().stream().flatMap(aPackage -> aPackage.getDeclared().stream()).toList();
   }

   @Override
   public Optional<Ap.Declared> getDeclared(String qualifiedName)
   {
      return ofNullable(adapt(getApi()).toElements().getTypeElement(getElement(), qualifiedName))
            .map(typeElement -> Adapters.adapt(getApi(), typeElement));
   }

   @Override
   public boolean isOpen()
   {
      return getElement().isOpen();
   }

   @Override
   public boolean isUnnamed()
   {
      return getElement().isUnnamed();
   }

   @Override
   public boolean isAutomatic()
   {
      return adapt(getApi()).toElements().isAutomaticModule(getElement());
   }

   @Override
   public List<Ap.Directive> getDirectives()
   {
      return getElement().getDirectives()
                         .stream()
                         .map(directive ->
                                    switch (directive.getKind())
                                    {
                                       case REQUIRES -> adapt(getApi(), ((ModuleElement.RequiresDirective) directive));
                                       case EXPORTS -> adapt(getApi(), ((ModuleElement.ExportsDirective) directive));
                                       case OPENS -> adapt(getApi(), ((ModuleElement.OpensDirective) directive));
                                       case USES -> adapt(getApi(), ((ModuleElement.UsesDirective) directive));
                                       case PROVIDES -> adapt(getApi(), ((ModuleElement.ProvidesDirective) directive));
                                    })
                         .collect(of((Supplier<List<Ap.Directive>>) ArrayList::new,
                                     (directives, directive) ->
                                     {
                                        if (!(directive instanceof Ap.Provides provides))
                                        {
                                           directives.add(directive);
                                           return;
                                        }

                                        Optional<Ap.Provides> existing =
                                              directives.stream()
                                                        .filter(Ap.Provides.class::isInstance)
                                                        .map(Ap.Provides.class::cast)
                                                        .filter(collected -> collected.getService().representsSameType(provides.getService()))
                                                        .findAny();

                                        if (existing.isEmpty())
                                        {
                                           directives.add(directive);
                                           return;
                                        }
                                        if (existing.get().getImplementations().size() > provides.getImplementations().size())
                                        {
                                           return;
                                        }
                                        int index = directives.indexOf(existing.get());
                                        directives.remove(index);
                                        directives.add(index, directive);
                                     },
                                     (directives, directives2) ->
                                     {
                                        directives.addAll(directives2);
                                        return directives;
                                     },
                                     Collections::unmodifiableList,
                                     IDENTITY_FINISH));
   }

   @Override
   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   @Override
   public Optional<String> getJavaDoc()
   {
      return ofNullable(adapt(getApi()).toElements().getDocComment(getElement()));
   }

   @Override
   public List<Ap.AnnotationUsage> getAnnotationUsages()
   {
      return Adapters.adapt(getApi(), adapt(getApi()).toElements().getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<Ap.AnnotationUsage> getDirectAnnotationUsages()
   {
      return adapt(getApi(), getElement().getAnnotationMirrors());
   }

   public NoType getMirror()
   {
      return noType;
   }

   public Ap.Context getApi()
   {
      return context;
   }

   @Override
   public String renderModuleInfo(RenderingContext renderingContext)
   {
      List<? extends Ap.Directive> directives = getDirectives();

      return moduleInfo().annotate(getAnnotationUsages())
                         .name(getName())
                         .requires(directives.stream().filter(Ap.Requires.class::isInstance).map(Ap.Requires.class::cast).toList())
                         .exports(directives.stream().filter(Ap.Exports.class::isInstance).map(Ap.Exports.class::cast).toList())
                         .opens(directives.stream().filter(Ap.Opens.class::isInstance).map(Ap.Opens.class::cast).toList())
                         .uses(directives.stream().filter(Ap.Uses.class::isInstance).map(Ap.Uses.class::cast).toList())
                         .provides(directives.stream().filter(Ap.Provides.class::isInstance).map(Ap.Provides.class::cast).toList())
                         .renderModuleInfo(renderingContext);
   }

   @Override
   public String renderQualifiedName(RenderingContext renderingContext)
   {
      return getQualifiedName();
   }

   @Override
   public String toString()
   {
      return getElement().toString();
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getQualifiedName());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Ap.Module otherModule))
      {
         return false;
      }
      return Objects.equals(getQualifiedName(), otherModule.getQualifiedName());
   }
}
