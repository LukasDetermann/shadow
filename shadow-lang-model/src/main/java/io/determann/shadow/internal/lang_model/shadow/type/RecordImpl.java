package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.adapter.LM_Adapters;
import io.determann.shadow.api.lang_model.adapter.LM_TypeAdapter;
import io.determann.shadow.api.lang_model.shadow.structure.LM_RecordComponent;
import io.determann.shadow.api.lang_model.shadow.type.LM_Generic;
import io.determann.shadow.api.lang_model.shadow.type.LM_Record;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.implementation.support.api.shadow.type.RecordSupport;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static io.determann.shadow.api.lang_model.adapter.LM_Adapters.adapt;
import static java.util.Arrays.stream;

public class RecordImpl extends DeclaredImpl implements LM_Record
{
   public RecordImpl(LM_Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public RecordImpl(LM_Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public List<LM_RecordComponent> getRecordComponents()
   {
      return getElement().getRecordComponents()
                         .stream()
                         .map(recordComponentElement -> adapt(getApi(), recordComponentElement))
                         .toList();
   }

   @Override
   public List<LM_Type> getGenericTypes()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> LM_Adapters.adapt(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<LM_Generic> getGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> LM_Adapters.adapt(getApi(), element))
                         .toList();
   }

   @Override
   public LM_Record withGenerics(LM_Type... generics)
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
            .map(LM_Adapters::adapt)
            .map(LM_TypeAdapter::toTypeMirror)
            .toArray(TypeMirror[]::new);

      return (LM_Record) adapt(getApi(), adapt(getApi()).toTypes().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public LM_Record withGenerics(String... qualifiedGenerics)
   {
      return withGenerics(stream(qualifiedGenerics)
                                .map(name -> getApi().getDeclaredOrThrow(name))
                                .toArray(LM_Type[]::new));
   }

   @Override
   public LM_Record interpolateGenerics()
   {
      return (LM_Record) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().capture(getMirror())));
   }

   @Override
   public LM_Record erasure()
   {
      return (LM_Record) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().erasure(getMirror())));
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
