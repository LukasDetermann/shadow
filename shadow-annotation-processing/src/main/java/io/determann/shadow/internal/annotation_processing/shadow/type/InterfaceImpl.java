package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.annotation_processing.adapter.TypeAdapter;
import io.determann.shadow.implementation.support.api.shadow.type.InterfaceSupport;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static java.util.Arrays.stream;

public class InterfaceImpl extends DeclaredImpl implements AP.Interface
{
   public InterfaceImpl(AP.Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public InterfaceImpl(AP.Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }



   @Override
   public boolean isFunctional()
   {
      return adapt(getApi()).toElements().isFunctionalInterface(getElement());
   }

   @Override
   public List<AP.Declared> getPermittedSubTypes()
   {
      return getElement().getPermittedSubclasses()
                         .stream()
                         .map(typeMirror -> adapt(getApi(), ((DeclaredType) typeMirror)))
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
   public AP.Interface withGenerics(AP.Type... generics)
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

      return (AP.Interface) adapt(getApi(), adapt(getApi()).toTypes().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public AP.Interface withGenerics(String... qualifiedGenerics)
   {
      return withGenerics(stream(qualifiedGenerics)
            .map(name -> getApi().getDeclaredOrThrow(name))
            .toArray(AP.Type[]::new));
   }

   @Override
   public AP.Interface interpolateGenerics()
   {
      return (AP.Interface) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().capture(getMirror())));
   }

   @Override
   public AP.Interface erasure()
   {
      return (AP.Interface) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().erasure(getMirror())));
   }

   @Override
   public boolean equals(Object other)
   {
      return InterfaceSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return InterfaceSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return InterfaceSupport.toString(this);
   }
}
