package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.adapter.LM_Adapters;
import io.determann.shadow.api.lang_model.shadow.LM_AnnotationUsage;
import io.determann.shadow.api.lang_model.shadow.directive.LM_Directive;
import io.determann.shadow.api.lang_model.shadow.directive.LM_Provides;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Module;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Package;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.shadow.directive.C_Provides;
import io.determann.shadow.api.shadow.structure.C_Module;
import io.determann.shadow.api.shadow.type.C_Type;

import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.NoType;
import java.util.*;
import java.util.function.Supplier;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.lang_model.LM_Queries.query;
import static io.determann.shadow.api.lang_model.adapter.LM_Adapters.adapt;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;
import static java.util.stream.Collector.of;

public class ModuleImpl implements LM_Module
{
   private final ModuleElement moduleElement;
   private final NoType noType;
   private final LM_Context context;


   public ModuleImpl(LM_Context context, ModuleElement moduleElement)
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
   public List<LM_Package> getPackages()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .map(PackageElement.class::cast)
                         .map(element -> LM_Adapters.adapt(getApi(), element))
                         .toList();
   }

   @Override
   public String getQualifiedName()
   {
      return getElement().getQualifiedName().toString();
   }

   @Override
   public List<LM_Declared> getDeclared()
   {
      return getPackages().stream().flatMap(aPackage -> query(aPackage).getDeclared().stream()).toList();
   }

   @Override
   public Optional<LM_Declared> getDeclared(String qualifiedName)
   {
      return ofNullable(adapt(getApi()).toElements().getTypeElement(getElement(), qualifiedName))
            .map(typeElement -> LM_Adapters.adapt(getApi(), typeElement));
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
   public List<LM_Directive> getDirectives()
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
                         .map(LM_Directive.class::cast)
                         .collect(of((Supplier<List<LM_Directive>>) ArrayList::new,
                                     (directives, directive) ->
                                     {
                                        if (!(directive instanceof LM_Provides provides))
                                        {
                                           directives.add(directive);
                                           return;
                                        }

                                        Optional<C_Provides> existing =
                                              directives.stream()
                                                        .filter(C_Provides.class::isInstance)
                                                        .map(C_Provides.class::cast)
                                                        .filter(collected -> query((C_Type) requestOrThrow(collected, PROVIDES_GET_SERVICE)).representsSameType(requestOrThrow(provides, PROVIDES_GET_SERVICE)))
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
   public List<LM_AnnotationUsage> getAnnotationUsages()
   {
      return LM_Adapters.adapt(getApi(), adapt(getApi()).toElements().getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<LM_AnnotationUsage> getDirectAnnotationUsages()
   {
      return LM_Adapters.adapt(getApi(), getElement().getAnnotationMirrors());
   }

   public NoType getMirror()
   {
      return noType;
   }

   public LM_Context getApi()
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
      if (!(other instanceof C_Module otherModule))
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
