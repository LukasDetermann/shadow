package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.module.DirectiveConverter;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.api.shadow.module.*;
import io.determann.shadow.impl.annotation_processing.shadow.module.*;

import javax.lang.model.element.ModuleElement;
import javax.lang.model.type.NoType;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static io.determann.shadow.api.ShadowApi.convert;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collector.of;
import static java.util.stream.Collectors.toUnmodifiableList;

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
      this.moduleElement = MirrorAdapter.getProcessingEnv(getApi()).getElementUtils().getModuleElement(noType.toString());
      if (moduleElement == null)
      {
         throw new IllegalStateException(noType + " is not unique");
      }
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      return MirrorAdapter.getModifiers(getElement());
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.MODULE;
   }

   public ModuleElement getElement()
   {
      return moduleElement;
   }

   @Override
   public String getQualifiedName()
   {
      return getElement().getQualifiedName().toString();
   }

   @Override
   public List<Package> getPackages()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .map(element -> MirrorAdapter.<Package>getShadow(getApi(), element))
                         .collect(toUnmodifiableList());
   }

   @Override
   public List<Declared> getDeclared()
   {
      return getPackages().stream().flatMap(aPackage -> aPackage.getDeclared().stream()).collect(toUnmodifiableList());
   }

   @Override
   public Optional<Declared> getDeclared(String qualifiedName)
   {
      return ofNullable(MirrorAdapter.getProcessingEnv(getApi()).getElementUtils().getTypeElement(qualifiedName))
            .map(typeElement -> MirrorAdapter.getShadow(getApi(), typeElement));
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
   public List<Directive> getDirectives()
   {
      return getElement().getDirectives()
                         .stream()
                         .map(directive ->
                              {
                                 switch (directive.getKind())
                                 {
                                    case REQUIRES:
                                       return new RequiresImpl(getApi(), ((ModuleElement.RequiresDirective) directive));
                                    case EXPORTS:
                                       return new ExportsImpl(getApi(), ((ModuleElement.ExportsDirective) directive));
                                    case OPENS:
                                       return new OpensImpl(getApi(), ((ModuleElement.OpensDirective) directive));
                                    case USES:
                                       return new UsesImpl(getApi(), ((ModuleElement.UsesDirective) directive));
                                    case PROVIDES:
                                       return new ProvidesImpl(getApi(), ((ModuleElement.ProvidesDirective) directive));
                                    default:
                                       throw new IllegalArgumentException();
                                 }
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
                                     Collector.Characteristics.IDENTITY_FINISH));
   }

   @Override
   public <T> List<T> mapDirectives(DirectiveMapper<T> mapper)
   {
      return getDirectives().stream()
                            .map(directive ->
                                 {
                                    switch (directive.getKind())
                                    {
                                       case REQUIRES:
                                          return mapper.requires(convert(directive).toRequiresOrThrow());
                                       case EXPORTS:
                                          return mapper.exports(convert(directive).toExportsOrThrow());
                                       case OPENS:
                                          return mapper.opens(convert(directive).toOpensOrThrow());
                                       case USES:
                                          return mapper.uses(convert(directive).toUsesOrThrow());
                                       case PROVIDES:
                                          return mapper.provides(convert(directive).toProvidesOrThrow());
                                       default:
                                          throw new IllegalArgumentException();
                                    }
                                 })
                            .collect(toUnmodifiableList());
   }

   @Override
   public void consumeDirectives(DirectiveConsumer consumer)
   {
      getDirectives().forEach(directive ->
                              {
                                 switch (directive.getKind())
                                 {
                                    case REQUIRES:
                                       consumer.requires(convert(directive).toRequiresOrThrow());
                                       break;
                                    case EXPORTS:
                                       consumer.exports(convert(directive).toExportsOrThrow());
                                       break;
                                    case OPENS:
                                       consumer.opens(convert(directive).toOpensOrThrow());
                                       break;
                                    case USES:
                                       consumer.uses(convert(directive).toUsesOrThrow());
                                       break;
                                    case PROVIDES:
                                       consumer.provides(convert(directive).toProvidesOrThrow());
                                       break;
                                 }
                              });
   }

   //com.sun.tools.javac.code.Types.TypeRelation#visitType throes exceptions for modules
   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return equals(shadow);
   }

   @Override
   public Module getModule()
   {
      return MirrorAdapter.getModule(getApi(), getElement());
   }

   @Override
   public String getSimpleName()
   {
      return MirrorAdapter.getSimpleName(getElement());
   }

   @Override
   public String getJavaDoc()
   {
      return MirrorAdapter.getJavaDoc(getApi(), getElement());
   }

   @Override
   public void logError(String msg)
   {
      MirrorAdapter.logError(getApi(), getElement(), msg);
   }

   @Override
   public void logInfo(String msg)
   {
      MirrorAdapter.logInfo(getApi(), getElement(), msg);
   }

   @Override
   public void logWarning(String msg)
   {
      MirrorAdapter.logWarning(getApi(), getElement(), msg);
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return MirrorAdapter.getAnnotationUsages(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return MirrorAdapter.getDirectAnnotationUsages(getApi(), getElement());
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
