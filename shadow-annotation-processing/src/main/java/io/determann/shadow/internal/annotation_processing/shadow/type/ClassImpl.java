package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.annotation_processing.adapter.TypeAdapter;
import io.determann.shadow.internal.annotation_processing.shadow.structure.PropertyFactory;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static java.util.Arrays.stream;

public class ClassImpl extends DeclaredImpl implements Ap.Class
{
   public ClassImpl(Ap.Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public ClassImpl(Ap.Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public boolean isAssignableFrom(C.Type type)
   {
      return adapt(getApi()).toTypes().isAssignable(getMirror(), adapt((Ap.Type) type).toTypeMirror());
   }

   @Override
   public Ap.Class getSuperClass()
   {
      TypeMirror superclass = getElement().getSuperclass();
      if (TypeKind.NONE.equals(superclass.getKind()))
      {
         return null;
      }
      return (Ap.Class) adapt(getApi(), ((DeclaredType) superclass));
   }

   @Override
   public List<Ap.Class> getPermittedSubClasses()
   {
      return getElement().getPermittedSubclasses()
                         .stream()
                         .map(typeMirror -> adapt(getApi(), ((DeclaredType) typeMirror)))
                         .map(Ap.Class.class::cast)
                         .toList();
   }

   @Override
   public List<Ap.Property> getProperties()
   {
      return PropertyFactory.of(getApi(), this);
   }

   @Override
   public List<Ap.Type> getGenericUsages()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> Adapters.<Ap.Type>adapt(getApi(), typeMirror))
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
   public Ap.Class withGenerics(Ap.Type... generics)
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
      Optional<Ap.Declared> outerType = getSurrounding();
      if (!isStatic() && outerType.isPresent() &&
          (outerType.get() instanceof Ap.Interface anInterface &&
           !anInterface.getGenericDeclarations().isEmpty() ||
           outerType.get() instanceof Ap.Class aClass1 && !aClass1.getGenericDeclarations().isEmpty()))
      {
         throw new IllegalArgumentException("cant add generics to " +
                                            getQualifiedName() +
                                            " when the class is not static and the outer class has generics");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(Adapters::adapt)
            .map(TypeAdapter::toTypeMirror)
            .toArray(TypeMirror[]::new);

      return (Ap.Class) adapt(getApi(), adapt(getApi()).toTypes().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public Ap.Class withGenerics(String... qualifiedGenerics)
   {
      return withGenerics(stream(qualifiedGenerics)
                                .map(name -> getApi().getDeclaredOrThrow(name))
                                .toArray(Ap.Type[]::new));
   }

   @Override
   public Ap.Class interpolateGenerics()
   {
      return (Ap.Class) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().capture(getMirror())));
   }

   @Override
   public Ap.Primitive asUnboxed()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().unboxedType(getMirror()));
   }

   @Override
   public Ap.Class erasure()
   {
      return (Ap.Class) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().erasure(getMirror())));
   }

   @Override
   public boolean equals(Object other)
   {
      return equals(Ap.Class.class, other);
   }

   @Override
   public String toString()
   {
      return toString("Class");
   }
}
