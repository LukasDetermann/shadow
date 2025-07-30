package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;

import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.NoType;
import java.util.*;
import java.util.function.Supplier;

import static io.determann.shadow.api.lang_model.Queries.query;
import static io.determann.shadow.api.lang_model.adapter.Adapters.adapt;
import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;
import static java.util.stream.Collector.of;

public class ModuleImpl implements LM.Module
{
   private final ModuleElement moduleElement;
   private final NoType noType;
   private final LM.Context context;


   public ModuleImpl(LM.Context context, ModuleElement moduleElement)
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
   public List<LM.Package> getPackages()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .map(PackageElement.class::cast)
                         .map(element -> Adapters.adapt(getApi(), element))
                         .toList();
   }

   @Override
   public String getQualifiedName()
   {
      return getElement().getQualifiedName().toString();
   }

   @Override
   public List<LM.Declared> getDeclared()
   {
      return getPackages().stream().flatMap(aPackage -> query(aPackage).getDeclared().stream()).toList();
   }

   @Override
   public Optional<LM.Declared> getDeclared(String qualifiedName)
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
   public List<LM.Directive> getDirectives()
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
                         .map(LM.Directive.class::cast)
                         .collect(of((Supplier<List<LM.Directive>>) ArrayList::new,
                                     (directives, directive) ->
                                     {
                                        if (!(directive instanceof LM.Provides provides))
                                        {
                                           directives.add(directive);
                                           return;
                                        }

                                        Optional<C.Provides> existing =
                                              directives.stream()
                                                        .filter(C.Provides.class::isInstance)
                                                        .map(C.Provides.class::cast)
                                                        .filter(collected -> query((C.Type) requestOrThrow(collected, PROVIDES_GET_SERVICE)).representsSameType(requestOrThrow(provides, PROVIDES_GET_SERVICE)))
                                                        .findAny();

                                        if (existing.isEmpty())
                                        {
                                           directives.add(directive);
                                           return;
                                        }
                                        if (requestOrThrow(existing.get(),PROVIDES_GET_IMPLEMENTATIONS).size() > requestOrThrow(provides,PROVIDES_GET_IMPLEMENTATIONS).size())
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
   public String getJavaDoc()
   {
      return adapt(getApi()).toElements().getDocComment(getElement());
   }

   @Override
   public List<LM.AnnotationUsage> getAnnotationUsages()
   {
      return Adapters.adapt(getApi(), adapt(getApi()).toElements().getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<LM.AnnotationUsage> getDirectAnnotationUsages()
   {
      return Adapters.adapt(getApi(), getElement().getAnnotationMirrors());
   }

   public NoType getMirror()
   {
      return noType;
   }

   public LM.Context getApi()
   {
      return context;
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
      if (!(other instanceof C.Module otherModule))
      {
         return false;
      }
      return Objects.equals(getQualifiedName(), requestOrThrow(otherModule, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));
   }

   @Override
   public Implementation getImplementation()
   {
      return getApi().getImplementation();
   }
}
