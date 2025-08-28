package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.annotation_processing.adapter.TypeAdapter;
import io.determann.shadow.implementation.support.api.shadow.type.RecordSupport;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static java.util.Arrays.stream;

public class RecordImpl extends DeclaredImpl implements AP.Record
{
   public RecordImpl(AP.Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public RecordImpl(AP.Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public List<AP.RecordComponent> getRecordComponents()
   {
      return getElement().getRecordComponents()
                         .stream()
                         .map(recordComponentElement -> adapt(getApi(), recordComponentElement))
                         .toList();
   }

   @Override
   public List<AP.Type> getGenericTypes()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> adapt(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<AP.Generic> getGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> adapt(getApi(), element))
                         .toList();
   }

   @Override
   public AP.Record withGenerics(AP.Type... generics)
   {
      if (generics.length == 0 || getGenerics().size() != generics.length)
      {
         throw new IllegalArgumentException(getQualifiedName() +
                                            " has " +
                                            getGenerics().size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(Adapters::adapt)
            .map(TypeAdapter::toTypeMirror)
            .toArray(TypeMirror[]::new);

      return (AP.Record) adapt(getApi(), adapt(getApi()).toTypes().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public AP.Record withGenerics(String... qualifiedGenerics)
   {
      return withGenerics(stream(qualifiedGenerics)
                                .map(name -> getApi().getDeclaredOrThrow(name))
                                .toArray(AP.Type[]::new));
   }

   @Override
   public AP.Record interpolateGenerics()
   {
      return (AP.Record) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().capture(getMirror())));
   }

   @Override
   public AP.Record erasure()
   {
      return (AP.Record) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().erasure(getMirror())));
   }

   @Override
   public boolean equals(Object other)
   {
      return RecordSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return RecordSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return RecordSupport.toString(this);
   }
}
