package org.determann.shadow.impl.shadow;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.TypeKind;
import org.determann.shadow.api.shadow.Declared;
import org.determann.shadow.api.shadow.Module;
import org.determann.shadow.api.shadow.Package;
import org.determann.shadow.api.shadow.Shadow;
import org.determann.shadow.api.shadow.module.Directive;
import org.determann.shadow.api.shadow.module.DirectiveConsumer;
import org.determann.shadow.api.shadow.module.DirectiveMapper;
import org.determann.shadow.impl.shadow.module.*;
import org.jetbrains.annotations.UnmodifiableView;

import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

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
      this.moduleElement = getApi().getJdkApiContext().elements().getModuleElement(noType.toString());
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
                         .collect(toUnmodifiableList());
   }

   @Override
   public @UnmodifiableView List<Declared> getDeclared()
   {
      return getPackages().stream().flatMap(aPackage -> aPackage.getDeclared().stream()).collect(toUnmodifiableList());
   }

   @Override
   public Declared getDeclared(String qualifiedName)
   {
      TypeElement typeElement = getApi().getJdkApiContext().elements().getTypeElement(getElement(), qualifiedName);
      if (typeElement == null)
      {
         throw new IllegalArgumentException("no Declared found for \"" + qualifiedName + "\"");
      }
      return getApi().getShadowFactory().shadowFromElement(typeElement);
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
                         .collect(toUnmodifiableList());
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
                                          return mapper.requires(getApi().convert(directive).toRequires().orElseThrow());
                                       case EXPORTS:
                                          return mapper.exports(getApi().convert(directive).toExports().orElseThrow());
                                       case OPENS:
                                          return mapper.opens(getApi().convert(directive).toOpens().orElseThrow());
                                       case USES:
                                          return mapper.uses(getApi().convert(directive).toUses().orElseThrow());
                                       case PROVIDES:
                                          return mapper.provides(getApi().convert(directive).toProvides().orElseThrow());
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
                                       consumer.requires(getApi().convert(directive).toRequires().orElseThrow());
                                       break;
                                    case EXPORTS:
                                       consumer.exports(getApi().convert(directive).toExports().orElseThrow());
                                       break;
                                    case OPENS:
                                       consumer.opens(getApi().convert(directive).toOpens().orElseThrow());
                                       break;
                                    case USES:
                                       consumer.uses(getApi().convert(directive).toUses().orElseThrow());
                                       break;
                                    case PROVIDES:
                                       consumer.provides(getApi().convert(directive).toProvides().orElseThrow());
                                       break;
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
