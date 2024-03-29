package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.converter.module.DirectiveConverter;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.api.shadow.module.Directive;
import io.determann.shadow.api.shadow.module.DirectiveKind;
import io.determann.shadow.api.shadow.module.Provides;
import io.determann.shadow.internal.lang_model.shadow.module.*;

import javax.lang.model.element.ModuleElement;
import javax.lang.model.type.NoType;
import java.util.*;
import java.util.function.Supplier;

import static io.determann.shadow.api.converter.Converter.convert;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;
import static java.util.stream.Collector.of;

public class ModuleImpl extends ShadowImpl<NoType> implements Module
{
   private final ModuleElement moduleElement;

   public ModuleImpl(LangModelContext context, ModuleElement moduleElement)
   {
      super(context, (NoType) moduleElement.asType());
      this.moduleElement = moduleElement;
   }

   public ModuleImpl(LangModelContext context, NoType noType)
   {
      super(context, noType);
      this.moduleElement = LangModelAdapter.getElements(getApi()).getModuleElement(noType.toString());
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

   public ModuleElement getElement()
   {
      return moduleElement;
   }

   @Override
   public List<Package> getPackages()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .map(element -> LangModelAdapter.<Package>getShadow(getApi(), element))
                         .toList();
   }

   @Override
   public String getQualifiedName()
   {
      return getElement().getQualifiedName().toString();
   }

   @Override
   public List<Declared> getDeclared()
   {
      return getPackages().stream().flatMap(aPackage -> aPackage.getDeclared().stream()).toList();
   }

   @Override
   public Optional<Declared> getDeclared(String qualifiedName)
   {
      return ofNullable(LangModelAdapter.getElements(getApi()).getTypeElement(getElement(), qualifiedName))
            .map(typeElement -> LangModelAdapter.getShadow(getApi(), typeElement));
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
      return LangModelAdapter.getElements(getApi()).isAutomaticModule(getElement());
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
                                                        .map(Converter::convert)
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
                                     IDENTITY_FINISH));
   }

   //com.sun.tools.javac.code.Types.TypeRelation#visitType throes exceptions for modules
   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return equals(shadow);
   }

   @Override
   public String getName()
   {
      return LangModelAdapter.getName(getElement());
   }

   @Override
   public String getJavaDoc()
   {
      return LangModelAdapter.getJavaDoc(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return LangModelAdapter.getAnnotationUsages(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return LangModelAdapter.getDirectAnnotationUsages(getApi(), getElement());
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
      if (!(other instanceof Module otherModule))
      {
         return false;
      }
      return Objects.equals(getQualifiedName(), otherModule.getQualifiedName());
   }
}
