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
import static io.determann.shadow.api.annotation_processing.dsl.Dsl.innerRecord;
import static io.determann.shadow.api.annotation_processing.dsl.Dsl.record;
import static java.util.Arrays.stream;

public class RecordImpl
      extends DeclaredImpl
      implements Ap.Record
{
   public RecordImpl(Ap.Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public RecordImpl(Ap.Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public List<Ap.RecordComponent> getRecordComponents()
   {
      return getElement().getRecordComponents()
                         .stream()
                         .map(recordComponentElement -> adapt(getApi(), recordComponentElement))
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
   public Ap.Record withGenerics(Ap.Type... generics)
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

      return (Ap.Record) adapt(getApi(), adapt(getApi()).toTypes().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public Ap.Record withGenerics(String... qualifiedGenerics)
   {
      return withGenerics(stream(qualifiedGenerics)
                                .map(name -> getApi().getDeclaredOrThrow(name))
                                .toArray(Ap.Type[]::new));
   }

   @Override
   public Ap.Record interpolateGenerics()
   {
      return (Ap.Record) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().capture(getMirror())));
   }

   @Override
   public Ap.Record erasure()
   {
      return (Ap.Record) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().erasure(getMirror())));
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      return (switch (getNesting())
              {
                 case OUTER -> record().package_(getPackage());
                 case INNER -> innerRecord().outer(getSurrounding().orElseThrow());
              }).annotate(getDirectAnnotationUsages())
                .modifier(getModifiers())
                .name(getName())
                .component(getRecordComponents())
                .genericDeclaration(getGenericDeclarations())
                .genericUsage(getGenericUsages())
                .implements_(getDirectInterfaces())
                .field(getFields())
                .method(getMethods())
                .constructor(getConstructors())
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
                 case OUTER -> record().package_(getPackage());
                 case INNER -> innerRecord().outer(getSurrounding().orElseThrow());
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
      return equals(Ap.Record.class, other);
   }

   @Override
   public String toString()
   {
      return toString("Record");
   }
}
