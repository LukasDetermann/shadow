package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.module.DirectiveConverter;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.api.shadow.module.*;
import io.determann.shadow.impl.shadow.module.*;

import javax.lang.model.element.ModuleElement;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static io.determann.shadow.api.ShadowApi.convert;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collector.of;

public class ModuleImpl extends ShadowImpl<NoType> implements Module
{
   private final ModuleElement moduleElement;

   public ModuleImpl(ShadowApi shadowApi, ModuleElement moduleElement)
   {
      super(shadowApi, (NoType) moduleElement.asType());
      this.moduleElement = moduleElement;
   }

   public ModuleImpl(ShadowApi shadowApi, NoType noType)
   {
      super(shadowApi, noType);
      this.moduleElement = getApi().getJdkApiContext().getProcessingEnv().getElementUtils().getModuleElement(noType.toString());
      if (moduleElement == null)
      {
         throw new IllegalStateException(noType + " is not unique");
      }
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.MODULE;
   }

   @Override
   public ModuleElement getElement()
   {
      return moduleElement;
   }

   @Override
   public List<Package> getPackages()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .map(element -> getApi().getShadowFactory().<Package>shadowFromElement(element))
                         .toList();
   }

   @Override
   public List<Declared> getDeclared()
   {
      return getPackages().stream().flatMap(aPackage -> aPackage.getDeclared().stream()).toList();
   }

   @Override
   public Optional<Declared> getDeclared(String qualifiedName)
   {
      return ofNullable(getApi().getJdkApiContext().getProcessingEnv().getElementUtils().getTypeElement(qualifiedName))
            .map(typeElement -> getApi().getShadowFactory().shadowFromElement(typeElement));
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
      return getApi().getJdkApiContext().getProcessingEnv().getElementUtils().isAutomaticModule(getElement());
   }

   @Override
   public List<Directive> getDirectives()
   {
      return getElement().getDirectives()
                         .stream()
                         .map(directive ->
                                    switch (directive.getKind())
                                    {
                                       case REQUIRES -> new RequiresImpl(getApi(), ((ModuleElement.RequiresDirective) directive));
                                       case EXPORTS -> new ExportsImpl(getApi(), ((ModuleElement.ExportsDirective) directive));
                                       case OPENS -> new OpensImpl(getApi(), ((ModuleElement.OpensDirective) directive));
                                       case USES -> new UsesImpl(getApi(), ((ModuleElement.UsesDirective) directive));
                                       case PROVIDES -> new ProvidesImpl(getApi(), ((ModuleElement.ProvidesDirective) directive));
                                    })
                         .map(Directive.class::cast)
                         .collect(of((Supplier<List<Directive>>) ArrayList::new,
                                     (directives, directive) ->
                                     {
                                        if (!directive.getKind().equals(DirectiveKind.PROVIDES))
                                        {
                                           directives.add(directive);
                                           return;
                                        }
                                        Provides provides = convert(directive).toProvidesOrThrow();

                                        Optional<Provides> existing =
                                              directives.stream()
                                                        .filter(collected -> collected.getKind().equals(DirectiveKind.PROVIDES))
                                                        .map(ShadowApi::convert)
                                                        .map(DirectiveConverter::toProvidesOrThrow)
                                                        .filter(collected -> collected.getService().representsSameType(provides.getService()))
                                                        .findAny();

                                        if (existing.isEmpty())
                                        {
                                           directives.add(directive);
                                           return;
                                        }
                                        if (existing.get().getImplementations().size() > directives.size())
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
                                     Collector.Characteristics.IDENTITY_FINISH));
   }

   @Override
   public <T> List<T> mapDirectives(DirectiveMapper<T> mapper)
   {
      return getDirectives().stream()
                            .map(directive ->
                                       switch (directive.getKind())
                                             {
                                                case REQUIRES -> mapper.requires(convert(directive).toRequiresOrThrow());
                                                case EXPORTS -> mapper.exports(convert(directive).toExportsOrThrow());
                                                case OPENS -> mapper.opens(convert(directive).toOpensOrThrow());
                                                case USES -> mapper.uses(convert(directive).toUsesOrThrow());
                                                case PROVIDES -> mapper.provides(convert(directive).toProvidesOrThrow());
                                             })
                            .toList();
   }

   @Override
   public void consumeDirectives(DirectiveConsumer consumer)
   {
      getDirectives().forEach(directive ->
                              {
                                 switch (directive.getKind())
                                 {
                                    case REQUIRES -> consumer.requires(convert(directive).toRequiresOrThrow());
                                    case EXPORTS -> consumer.exports(convert(directive).toExportsOrThrow());
                                    case OPENS -> consumer.opens(convert(directive).toOpensOrThrow());
                                    case USES -> consumer.uses(convert(directive).toUsesOrThrow());
                                    case PROVIDES -> consumer.provides(convert(directive).toProvidesOrThrow());
                                 }
                              });
   }

   //com.sun.tools.javac.code.Types.TypeRelation#visitType throes exceptions for modules
   @Override
   public boolean representsSameType(Shadow<? extends TypeMirror> shadow)
   {
      return equals(shadow);
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
      if (other == null || !getClass().equals(other.getClass()))
      {
         return false;
      }
      ModuleImpl otherModule = (ModuleImpl) other;
      return Objects.equals(getQualifiedName(), otherModule.getQualifiedName());
   }
}
