package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.RecordComponent;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static java.util.Arrays.stream;

public class RecordImpl extends DeclaredImpl implements Record
{
   public RecordImpl(ShadowApi shadowApi, DeclaredType declaredTypeMirror)
   {
      super(shadowApi, declaredTypeMirror);
   }

   public RecordImpl(ShadowApi shadowApi, TypeElement typeElement)
   {
      super(shadowApi, typeElement);
   }

   @Override
   public RecordComponent getRecordComponentOrThrow(String simpleName)
   {
      return getRecordComponents().stream().filter(field -> field.getSimpleName().equals(simpleName)).findAny().orElseThrow();
   }

   @Override
   public List<RecordComponent> getRecordComponents()
   {
      return getElement().getRecordComponents()
                         .stream()
                         .map(recordComponentElement -> MirrorAdapter.<RecordComponent>getShadow(getApi(), recordComponentElement))
                         .toList();
   }

   @Override
   public Record withGenerics(Shadow... generics)
   {
      if (generics.length == 0 || getFormalGenerics().size() != generics.length)
      {
         throw new IllegalArgumentException(getQualifiedName() +
                                            " has " +
                                            getFormalGenerics().size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(MirrorAdapter::getType)
            .toArray(TypeMirror[]::new);

      return MirrorAdapter
                     .getShadow(getApi(),
                                MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public Record withGenerics(String... qualifiedGenerics)
   {
      return withGenerics(stream(qualifiedGenerics)
                                .map(qualifiedName -> getApi().getDeclaredOrThrow(qualifiedName))
                                .toArray(Shadow[]::new));
   }

   @Override
   public List<Shadow> getGenerics()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> MirrorAdapter.<Shadow>getShadow(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<Generic> getFormalGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> MirrorAdapter.<Generic>getShadow(getApi(), element))
                         .toList();
   }

   @Override
   public Record interpolateGenerics()
   {
      return MirrorAdapter.getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().capture(getMirror()));
   }

   @Override
   public Record erasure()
   {
      return MirrorAdapter.getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().erasure(getMirror()));
   }
}
