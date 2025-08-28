package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.annotation_processing.adapter.TypeAdapter;
import io.determann.shadow.implementation.support.api.shadow.structure.PropertySupport;
import io.determann.shadow.implementation.support.api.shadow.type.ClassSupport;
import io.determann.shadow.internal.annotation_processing.shadow.structure.PropertyImpl;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static java.util.Arrays.stream;

public class ClassImpl extends DeclaredImpl implements AP.Class
{
   public ClassImpl(AP.Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public ClassImpl(AP.Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public boolean isAssignableFrom(C.Type type)
   {
      return adapt(getApi()).toTypes().isAssignable(getMirror(), adapt((AP.Type) type).toTypeMirror());
   }

   @Override
   public AP.Class getSuperClass()
   {
      TypeMirror superclass = getElement().getSuperclass();
      if (TypeKind.NONE.equals(superclass.getKind()))
      {
         return null;
      }
      return (AP.Class) adapt(getApi(), ((DeclaredType) superclass));
   }

   @Override
   public List<AP.Class> getPermittedSubClasses()
   {
      return getElement().getPermittedSubclasses()
                         .stream()
                         .map(typeMirror -> adapt(getApi(), ((DeclaredType) typeMirror)))
                         .map(AP.Class.class::cast)
                         .toList();
   }

   @Override
   public List<AP.Property> getProperties()
   {
      return PropertySupport.propertiesOf(this)
                            .stream()
                            .map(delegate -> new PropertyImpl(getApi(), delegate))
                            .map(AP.Property.class::cast)
                            .toList();
   }

   @Override
   public Optional<AP.Declared> getOuterType()
   {
      TypeMirror enclosingType = getMirror().getEnclosingType();
      if (enclosingType.getKind().equals(TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(adapt(getApi(), ((DeclaredType) enclosingType)));
   }

   @Override
   public List<AP.Type> getGenericTypes()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> Adapters.<AP.Type>adapt(getApi(), typeMirror))
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
   public AP.Class withGenerics(AP.Type... generics)
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
      Optional<AP.Declared> outerType = getOuterType();
      if (outerType.isPresent() &&
          (outerType.get() instanceof AP.Interface anInterface &&
           !anInterface.getGenerics().isEmpty() ||
           outerType.get() instanceof AP.Class aClass1 && !aClass1.getGenerics().isEmpty()))
      {
         throw new IllegalArgumentException("cant add generics to " +
                                            getQualifiedName() +
                                            " when the class is not static and the outer class has generics");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(Adapters::adapt)
            .map(TypeAdapter::toTypeMirror)
            .toArray(TypeMirror[]::new);

      return (AP.Class) adapt(getApi(), adapt(getApi()).toTypes().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public AP.Class withGenerics(String... qualifiedGenerics)
   {
      return withGenerics(stream(qualifiedGenerics)
                                .map(name -> getApi().getDeclaredOrThrow(name))
                                .toArray(AP.Type[]::new));
   }

   @Override
   public AP.Class interpolateGenerics()
   {
      return (AP.Class) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().capture(getMirror())));
   }

   @Override
   public AP.Primitive asUnboxed()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().unboxedType(getMirror()));
   }

   @Override
   public AP.Class erasure()
   {
      return (AP.Class) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().erasure(getMirror())));
   }

   @Override
   public boolean equals(Object other)
   {
      return ClassSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return ClassSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return ClassSupport.toString(this);
   }
}
