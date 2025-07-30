package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;
import io.determann.shadow.api.lang_model.adapter.TypeAdapter;
import io.determann.shadow.implementation.support.api.shadow.type.RecordSupport;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static io.determann.shadow.api.lang_model.adapter.Adapters.adapt;
import static java.util.Arrays.stream;

public class RecordImpl extends DeclaredImpl implements LM.Record
{
   public RecordImpl(LM.Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public RecordImpl(LM.Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public List<LM.RecordComponent> getRecordComponents()
   {
      return getElement().getRecordComponents()
                         .stream()
                         .map(recordComponentElement -> adapt(getApi(), recordComponentElement))
                         .toList();
   }

   @Override
   public List<LM.Type> getGenericTypes()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> Adapters.adapt(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<LM.Generic> getGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> Adapters.adapt(getApi(), element))
                         .toList();
   }

   @Override
   public LM.Record withGenerics(LM.Type... generics)
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

      return (LM.Record) adapt(getApi(), adapt(getApi()).toTypes().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public LM.Record withGenerics(String... qualifiedGenerics)
   {
      return withGenerics(stream(qualifiedGenerics)
                                .map(name -> getApi().getDeclaredOrThrow(name))
                                .toArray(LM.Type[]::new));
   }

   @Override
   public LM.Record interpolateGenerics()
   {
      return (LM.Record) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().capture(getMirror())));
   }

   @Override
   public LM.Record erasure()
   {
      return (LM.Record) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().erasure(getMirror())));
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
