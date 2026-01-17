package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.annotation_processing.adapter.TypeAdapter;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static io.determann.shadow.api.annotation_processing.dsl.JavaDsl.innerInterface;
import static io.determann.shadow.api.annotation_processing.dsl.JavaDsl.interface_;
import static java.util.Arrays.stream;

public class InterfaceImpl
      extends DeclaredImpl
      implements Ap.Interface
{
   public InterfaceImpl(Ap.Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public InterfaceImpl(Ap.Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }


   @Override
   public boolean isFunctional()
   {
      return adapt(getApi()).toElements().isFunctionalInterface(getElement());
   }

   @Override
   public List<Ap.Declared> getPermittedSubTypes()
   {
      return getElement().getPermittedSubclasses()
                         .stream()
                         .map(typeMirror -> adapt(getApi(), ((DeclaredType) typeMirror)))
                         .toList();
   }

   @Override
   public List<Ap.Type> getGenericUsages()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> adapt(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<Ap.Generic> getGenericDeclarations()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> adapt(getApi(), element))
                         .toList();
   }

   @Override
   public Ap.Interface withGenerics(Ap.Type... generics)
   {
      if (generics.length == 0 || getGenericDeclarations().size() != generics.length)
      {
         throw new IllegalArgumentException(getQualifiedName() +
                                            " has " +
                                            getGenericDeclarations().size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(Adapters::adapt)
            .map(TypeAdapter::toTypeMirror)
            .toArray(TypeMirror[]::new);

      return (Ap.Interface) adapt(getApi(), adapt(getApi()).toTypes().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public Ap.Interface withGenerics(String... qualifiedGenerics)
   {
      return withGenerics(stream(qualifiedGenerics)
                                .map(name -> getApi().getDeclaredOrThrow(name))
                                .toArray(Ap.Type[]::new));
   }

   @Override
   public Ap.Interface interpolateGenerics()
   {
      return (Ap.Interface) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().capture(getMirror())));
   }

   @Override
   public Ap.Interface erasure()
   {
      return (Ap.Interface) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().erasure(getMirror())));
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      return (switch (getNesting())
              {
                 case OUTER -> interface_().package_(getPackage());
                 case INNER -> innerInterface().outer(getSurrounding().orElseThrow());
              }).annotate(getDirectAnnotationUsages())
                .modifier(getModifiers())
                .name(getName())
                .genericDeclaration(getGenericDeclarations())
                .genericUsage(getGenericUsages())
                .extends_(getDirectInterfaces())
                .permits(getPermittedSubTypes())
                .field(getFields())
                .method(getMethods())
                .renderDeclaration(renderingContext);
   }

   @Override
   public String renderQualifiedName(RenderingContext renderingContext)
   {
      return getQualifiedName();
   }

   @Override
   public String renderSimpleName(RenderingContext renderingContext)
   {
      return getName();
   }

   @Override
   public String renderType(RenderingContext renderingContext)
   {
      return (switch (getNesting())
              {
                 case OUTER -> interface_().package_(getPackage());
                 case INNER -> innerInterface().outer(getSurrounding().orElseThrow());
              }).name(getName())
                .genericDeclaration(getGenericDeclarations())
                .genericUsage(getGenericUsages())
                .renderType(renderingContext);
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      return renderingContext.renderName(renderQualifiedName(renderingContext));
   }

   @Override
   public boolean equals(Object other)
   {
      return equals(Ap.Interface.class, other);
   }

   @Override
   public String toString()
   {
      return toString("Interface");
   }
}
